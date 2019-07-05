package it.polimi.ingsw.controller;

import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.weapons.*;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Controller that handles the shooting operations
 */
public class ShootController {
    private static final Logger LOG = Logger.getLogger(ShootController.class.getName());

    /**
     * Map that matches the remotePlayers with their nicknames
     */
    private Map<String, RemotePlayer> remoteUsers;
    /**
     * The match going on
     */
    private Match match;
    /**
     * Controller for paying the costs
     */
    private PaymentGateway paymentGateway;
    /**
     * List containing the players that might be inherited as targets for following effects
     */
    private List<Player> inheritedPlayerTargets;
    /**
     * Player that already took damage this turn
     */
    private List<Player> alreadyShotTargets;
    /**
     * In case a square has been fixed for following effects, it is saved here
     */
    private Square targetFixedSquare;
    /**
     * Update people
     */
    private Updater updater;
    /**
     * List of players that might want to use their tagback grenades
     */
    private List<Player> mayUseTagback;
    /**
     * List of players that has already been targeted by a targeting scope this turn
     */
    private List<Player> alreadyScoped;
    /**
     * Controller of the powerups
     */
    private PowerUpController powerUpController;

    /**
     * Constructor
     * @param remoteUsers Map with the RemotePlayers and their nicknames
     * @param match The match going on
     */
    public ShootController(Map<String, RemotePlayer> remoteUsers, Match match){
        this.remoteUsers = remoteUsers;
        this.match = match;
        paymentGateway = new PaymentGateway(match);
        updater = new Updater(remoteUsers, match);
        powerUpController = new PowerUpController(match, remoteUsers);
    }

    /**
     * Initialises the controller for a new shoot
     */
    public void initShoot(){
        inheritedPlayerTargets = new ArrayList<>();
        alreadyShotTargets = new ArrayList<>();
        mayUseTagback = new ArrayList<>();
        alreadyScoped = new ArrayList<>();
    }

    /**
     * Main method of the class, calls all the method needed to shoot people
     * @param player The player who wants to shoot
     * @param weapon The weapon the player is using
     * @throws RemoteException In case something goes wrong with the connection
     */
    public void shoot(Player player, Weapon weapon) throws RemoteException{
        Attack attack;

        initShoot();
        weapon.setCharged(false); // The weapon is no longer charged

        // Select the main attack
        attack = selectAttack(player, weapon);
        if(attack == null)
            return;
        doAttack(player, attack); // Do the thing

        // Now people might wanna use their tagback
        for(Player pl : mayUseTagback)
            tagbackRevenge(pl, player);
    }

    /**
     * When an attack has been selected, this method performs the attack and also checks for additional attacks that
     * might be used
     * @param player The player shooting
     * @param attack The attack selected
     * @throws RemoteException In case something goes wrong with the connection
     */
    public void doAttack(Player player, Attack attack) throws RemoteException{
        RemotePlayer remotePlayer = remoteUsers.get(player.getNickname());
        List<Boolean> additionalAttacksPerformed = new ArrayList<>();
        boolean noSelection = false;
        boolean bonusMovementExpended = false;

        // Init a thing that I need to keep track of additional attacks already performed
        for(int i=0; i<attack.getAdditionalAttacks().size(); i++)
            additionalAttacksPerformed.add(false);

        paymentGateway.payCost(attack.getCost(), player, remotePlayer); // A man must pay


        // Player might wanna use the bonus movement
        bonusMovementExpended = bonusMovement(player, attack, false);

        // Perform the attack
        applyTargets(player, attack);

        // Now we have to start consider the additional attacks
        // Cycle until: there are additional attacks to perform and the player has never selected 0 of them
        //              and all the additional attacks are performed
        while(attack.hasAdditionalAttacks() && !noSelection && additionalAttacksPerformed.contains(false)){
            // I can select only additional attacks that have never been made this turn
            List<Attack> selectableAdditional = attack.getAdditionalAttacks().stream()
                    .filter(atk -> additionalAttacksPerformed.get(attack.getAdditionalAttacks().indexOf(atk)).equals(false))
                    .filter(atk -> player.canPay(atk.getCost()))
                    .collect(Collectors.toList());

            if(!selectableAdditional.isEmpty()){ // If the list is empty, it's pointless to go on
                List<Attack> selected;
                // If there is only one selectable additional attack and it's cost is zero, then it's
                // auto-activated, since some weapons are tricky to do and we need this workaround
                if(selectableAdditional.size() == 1 && selectableAdditional.get(0).getCost().isEmpty()){
                    selected = selectableAdditional;
                }
                else{ // Standard path, go on with the attack selection
                    bonusMovementExpended = bonusMovement(player, attack, bonusMovementExpended);
                    selected = remotePlayer.selectIdentifiable(selectableAdditional, 0, 1, Dialog.WEAPON_ATTACK);
                }

                if(selected.isEmpty()) // If nothing has been selected, there's no point in continue prompting the player
                    noSelection = true;
                else{
                    doAttack(player, selected.get(0));
                    additionalAttacksPerformed.set(attack.getAdditionalAttacks().indexOf(selected.get(0)), true);
                }
            }
            else
                noSelection = true;
            bonusMovementExpended = bonusMovement(player, attack, bonusMovementExpended);
        }

        bonusMovement(player, attack, bonusMovementExpended);
    }

    /**
     * Makes the user select the attack it wants to use
     * @param player The player acting
     * @param weapon The weapon he is using
     * @return The selected attack if there are some, null if no attacks can be done
     * @throws RemoteException In case something goes wrong with the connection
     */
    public Attack selectAttack(Player player, Weapon weapon) throws RemoteException {
        RemotePlayer remotePlayer = remoteUsers.get(player.getNickname());

        List<Attack> usableAttacks = weapon.getAttacks().stream()
                .filter(atk -> player.canPay(atk.getCost()))
                .collect(Collectors.toList());

        // If no attacks are available, just return null
        if (usableAttacks.isEmpty())
            return null;
        else if (usableAttacks.size() == 1) // If there is only a possible choice, there is no need to ask the user
            return usableAttacks.get(0);

        return remotePlayer.selectIdentifiable(usableAttacks, 1, 1, Dialog.WEAPON_ATTACK).get(0);
    }

    /**
     * Handles the movement of a player based on the properties of the MovementEffect
     * @param source The player that activates the effect
     * @param target The target of the effect
     * @param movement The MovementEffect object
     * @throws RemoteException In case something goes wrong with the connection
     */
    public void manageMovement(Player source, Player target, MovementEffect movement) throws RemoteException{
        RemotePlayer remotePlayer = remoteUsers.get(source.getNickname());
        List<Square> possibleDestination;
        Square destSquare;

        if(movement.isFixed() && targetFixedSquare != null){
            destSquare = targetFixedSquare;
        } else{
            // Filter possible destinations
            if(movement.isLine())
                possibleDestination = target.getSquare().getSquaresByDistanceAligned(movement.getValue());
            else
                possibleDestination = target.getSquare().getSquaresByDistance(movement.getValue());

            if(movement.isVisibleDest().equals(Visibility.VISIBLE))
                possibleDestination = possibleDestination.stream().filter(sq -> target.getSquare().isVisible(sq)).collect(Collectors.toList());
            else if (movement.isVisibleDest().equals(Visibility.INVISIBLE))
                possibleDestination = possibleDestination.stream().filter(sq -> !target.getSquare().isVisible(sq)).collect(Collectors.toList());

            // Ask the user
            destSquare = remotePlayer.selectIdentifiable(possibleDestination, 1, 1, Dialog.MOVE).get(0);
        }

        // Actually move the target
        target.move(destSquare);
    }

    /**
     * Performs what is needed to do a BonusMovement
     * @param player The player who is playing now
     * @param attack The attack selected
     * @param bonusMovementExpended Whether the BonusMovement has already been used
     * @return The new value of bonusMovementExpended
     * @throws RemoteException In case something goes wrong with the connection
     */
    public boolean bonusMovement(Player player, Attack attack, boolean bonusMovementExpended) throws RemoteException{
        RemotePlayer remotePlayer = remoteUsers.get(player.getNickname());
        boolean expends = false;
        if(!bonusMovementExpended && attack.hasBonusMovement() && player.canPay(attack.getBonusMovementCost())){
            List<MovementEffect> mov = new ArrayList<>(attack.getBonusMovement());
            if(!remotePlayer.selectIdentifiable(mov, 0, 1 , Dialog.BONUS_MOVEMENT).isEmpty()){
                paymentGateway.payCost(attack.getBonusMovementCost(), player, remotePlayer);
                manageMovement(player, player, mov.get(0));
                expends = true;
                updater.updateModel(player.getNickname());
            }
        }

        return expends || bonusMovementExpended;
    }

    /**
     * Calls the right methods to handle the targets
     * @param player The player shooting
     * @param attack The attack selected
     * @throws RemoteException In case something goes wrong with the connection
     */
    public void applyTargets(Player player, Attack attack) throws RemoteException{
        List<Target> baseFire = attack.getBaseFire();

        for(Target targ : baseFire){
            // Player target
            if(targ.getClass().isAssignableFrom(PlayerTarget.class)){
                handlePlayerTarget(player, (PlayerTarget) targ);
            }
            else // Square target
            {
                handleSquareTarget(player, (SquareTarget) targ);
            }
        }

    }

    /**
     * Method used to select the square of a SquareTarget
     * @param player The player shooting
     * @param target The SquareTarget object
     * @return The list of squares selected
     * @throws RemoteException In case something goes wrong with the connection
     */
    public List<Square> squareSelection(Player player, SquareTarget target) throws RemoteException{
        List<Square> selected = new ArrayList<>();
        List<Square> validSquares;
        List<Square> tempSelection;
        RemotePlayer remotePlayer = remoteUsers.get(player.getNickname());

        while(selected.size() < target.getNumberOfTargets()){
            List<Square> toBeDeleted = new ArrayList<>();

            // Prepare a list containing all the squares that are singularly valid for the target
            validSquares = match.getGameBoard().getBoard().getAllSquares().stream()
                    .filter(sq -> target.validateTargetSquare(player, sq))
                    .collect(Collectors.toList());
            for(Square sq : selected) // Remove from there the already selected squares
                validSquares.remove(sq);

            for(Square sq : validSquares){  // For each valid square, let's see if it's compatible with the already selected
                tempSelection = new ArrayList<>(selected);
                tempSelection.add(sq);
                if(!target.compatibleTargetSquares(player, tempSelection))
                    toBeDeleted.add(sq);
            }

            // Remove the not-compatible squares
            for(Square sq : toBeDeleted)
                validSquares.remove(sq);

            // If there're valid squares left, ask the user to select one
            if(!validSquares.isEmpty())
                selected.add(remotePlayer.selectIdentifiable(validSquares, 1, 1, Dialog.TARGET_SQUARE).get(0));
            else
                break;
        }

        return selected;
    }

    /**
     * Method that handles a SquareTarget and calls the methods needed to apply the effects
     * @param player The player shooting
     * @param target The SquareTarget object
     * @throws RemoteException In case something goes wrong with the connection
     */
    public void handleSquareTarget(Player player, SquareTarget target) throws RemoteException{
        RemotePlayer remotePlayer = remoteUsers.get(player.getNickname());
        int maxPlayers = target.getNumberOfPlayers();
        boolean compatible = false;
        List<Player> enemies = match.getOtherPlayersAlive(player.getNickname());


        // Build the list of available squares to select
        List<Square> targetSquares = match.getGameBoard().getBoard().getAllSquares().stream()
                .filter(sq -> target.validateTargetSquare(player, sq))
                .collect(Collectors.toList());

        if(targetSquares.isEmpty() && !target.isInherited())
            return;

        List<Square> selectedSquares = new ArrayList<>();

        if(targetSquares.size() == 1){
            selectedSquares = targetSquares;
            compatible = true;
        }

        // If the target is inherited, just set as target the fixed square
        if(target.isInherited()){
            compatible = true;
            selectedSquares = Collections.singletonList(targetFixedSquare);
        }

        // Ask the user which squares he wants to target
        while (!compatible){
            // Square selection
            selectedSquares = squareSelection(player, target);
            if(target.compatibleTargetSquares(player, selectedSquares))
                compatible = true;
            else{
                remotePlayer.safeShowMessage(Dialog.TARGETS_NOT_COMPATIBLE);
            }
        }

        // If it's a room target, I must select all the squares in that room
        if(!selectedSquares.isEmpty() && target.isRoom())
            selectedSquares = selectedSquares.get(0).getRoomSquares();

        // If it's a vortex-target, make it fixed (in case of doubt, the nearest one)
        if(target.isVortex()){
            List<Square> sortedSquares = selectedSquares.stream()
                    .sorted(Comparator.comparingInt(sq -> sq.getDistance(player.getSquare())))
                    .collect(Collectors.toList());
            if(target.fixReverse()){
                targetFixedSquare = sortedSquares.get(sortedSquares.size()-1);
            }
            else
                targetFixedSquare = sortedSquares.get(0);
        }

        List<Player> targetPlayers = new ArrayList<>();
        List<Player> possibleTargets;
        int playerDist = target.getPlayerMaxDistance();

        possibleTargets = selectedSquares.stream()
                .flatMap(sq -> enemies.stream().filter(en -> en.getSquare() != null).filter(en -> en.getSquare().getDistance(sq) <= playerDist))
                .distinct()
                .collect(Collectors.toList());

        if(target.isExclusive())
        {
            possibleTargets = possibleTargets.stream()
                    .filter(pl -> !alreadyShotTargets.contains(pl))
                    .collect(Collectors.toList());
        }

        if(possibleTargets.isEmpty())
            return;

        if(target.isAOE()){ // If it's an AOE attack, I target all the possible players
                targetPlayers = new ArrayList<>(possibleTargets);
        }
        else{ // Otherwise for each target square, ask the user the player he wants to target
            for(Square sq : selectedSquares){ // Cycle through all the target squares
                possibleTargets = new ArrayList<>();
                for(Player en : enemies){ // Cycle through all the enemies
                    // If that enemy isn't already targeted by the attack and it's in a suitable position...
                    if(!targetPlayers.contains(en) && en.getSquare().getDistance(sq) <= playerDist)
                        possibleTargets.add(en); // Add him to the list of possible victims
                }
                // Ask the user for a target
                targetPlayers.addAll(remotePlayer.selectIdentifiable(possibleTargets, 0, maxPlayers, Dialog.SHOOT_TARGET));
            }
        }

        // Now I have all the target players, I can start apply the effects
        for(Player victim : targetPlayers){
            applyEffects(player, victim, target.getEffects());
        }
    }

    /**
     * @param player The player source of the attack
     * @param target The PlayerTarget object
     * @return A list containing all the players that can be hit by that target of the attack
     */
    public List<Player> getHittableEnemies(Player player, PlayerTarget target){
        List<Player> hittableEnemies = match.getOtherPlayers(player.getNickname());

        hittableEnemies = hittableEnemies.stream()
                .filter(en -> en.getSquare()!=null)
                .filter(en -> target.validateTargetPlayer(player, en))
                .collect(Collectors.toList());

        // Handle situation where the targets are exclusive, if the target is exclusive it means that I cannot hit
        // people already hit before
        if(target.isExclusive()){
            hittableEnemies = hittableEnemies.stream()
                    .filter(en -> !alreadyShotTargets.contains(en))
                    .collect(Collectors.toList());
        }

        // Handle situation where the targets are inherited, I have them in a List and when I hit them again I will
        // remove them from that list. Notice in case of inherited target I can only hit people already hit before
        if(target.isInherited()){
            hittableEnemies = new ArrayList<>(inheritedPlayerTargets);
        }

        return hittableEnemies;
    }

    /**
     * Method that handles a PlayerTarget and calls the methods needed to apply the effects
     * @param player The player shooting
     * @param target The PlayerTarget object
     * @throws RemoteException In case something goes wrong with the connection
     */
    public void handlePlayerTarget(Player player, PlayerTarget target) throws RemoteException {
        RemotePlayer remotePlayer = remoteUsers.get(player.getNickname());
        int max = target.getNumberOfTargets();
        boolean compatible = false;
        Player originPlayer;

        if(target.isChain() && !alreadyShotTargets.isEmpty()){
            originPlayer = alreadyShotTargets.get(alreadyShotTargets.size()-1);
        }
        else
            originPlayer = player;

        List<Player> enemies = getHittableEnemies(originPlayer, target);
        enemies = enemies.stream()
                .filter(p -> !p.equals(player))
                .collect(Collectors.toList());

        List<Player> targets = new ArrayList<>();

        // Checking special conditions
        if(target.isAOE()){
            compatible = true;
            targets = enemies;
        }

        // If there are no enemies available, there's no need to ask anything
        if(enemies.isEmpty())
            compatible = true;

        // Ask the player who he wants to attacks
        while(!compatible){
            targets = remotePlayer.selectIdentifiable(enemies, 1, max, Dialog.SHOOT_TARGET);
            if(!target.compatibleTargetPlayers(originPlayer, targets)){
                remotePlayer.safeShowMessage(Dialog.TARGETS_NOT_COMPATIBLE);
            }
            else
                compatible = true;
        }

        for(Player victim : targets){
            applyEffects(player, victim, target.getEffects());
        }

        // Final handling for inherited targets
        if(target.isInherited()){
            for(Player victim : targets)
                inheritedPlayerTargets.remove(victim);
        }
    }

    /**
     * Calls the methods to apply the affects on a target
     * @param source The player shooting
     * @param target The target of the player
     * @param effects The list of effects that should be applied
     * @throws RemoteException In case something goes wrong with the connection
     */
    public void applyEffects(Player source, Player target, List<Effect> effects) throws RemoteException{
        for(Effect effect : effects){
            if(effect.getClass().isAssignableFrom(HarmfulEffect.class)){ // If it's a harmful effect
                applyHarmful(source, target, (HarmfulEffect) effect);
            }
            else{ // Otherwise it is a movement effect
                MovementEffect mov = (MovementEffect) effect;
                if(mov.isSelf())
                    manageMovement(source, source, mov);
                else
                    manageMovement(source, target, mov);
            }
        }

        updater.updateModelToEveryone();
    }

    /**
     * Applies a harmful effect on target player
     * @param source The player shooting
     * @param target The target of the attack
     * @param effect The HarmfulEffect object
     */
    public void applyHarmful(Player source, Player target, HarmfulEffect effect){
        int value = effect.getValue();
        PlayerToken color = source.getColor();

        if(effect.getType().equals(HarmType.DAMAGE)){
            // Inflict damage
            target.takeDamage(color, value);
            // To keep memory of whom I did shot
            if(!alreadyShotTargets.contains(target))
                alreadyShotTargets.add(target);
            // Same, but with a different List, to handle inherited targets
            if(!inheritedPlayerTargets.contains(target))
                inheritedPlayerTargets.add(target);

            // Setup for tagback granade
            if(target.hasTagback() && target.getSquare().isVisible(source.getSquare()) && !mayUseTagback.contains(target))
                mayUseTagback.add(target);

            if(source.hasTargetingscope() && !alreadyScoped.contains(target))
                useTargetingscope(source, target);
        }
        else {
            // Add the marks
            target.addMark(color, value);
        }
    }

    /**
     * Handles the selection and usage of a tagback grenade
     * @param tagbacker The player that might wanna use the tagback grenade
     * @param currentPlayer The player acting in this turn
     */
    public void tagbackRevenge(Player tagbacker, Player currentPlayer){
        RemotePlayer remoteTagbacker = remoteUsers.get(tagbacker.getNickname());
        List<PowerUp> tagbackGrenades = tagbacker.getPowerUps().stream()
                .filter(pwu -> pwu.getType().equals(PowerUpType.TAGBACK_GRANADE))
                .collect(Collectors.toList());
        try{
            List<PowerUp> selected = remoteTagbacker.selectIdentifiable(tagbackGrenades, 0, 1, Dialog.TAGBACK_SELECT);
            if(!selected.isEmpty()){
                tagbacker.removePowerUp(selected.get(0));
                match.getGameBoard().getPowerUpDeck().discard(selected.get(0));
                currentPlayer.addMark(tagbacker.getColor(), 1);
            }
            updater.updateModel(tagbacker.getNickname());
        }catch(RemoteException e){
            // E vbb.
            LOG.log(Level.INFO,"Something happened when {0} tried to tagback, not a big deal...", tagbacker.getNickname());
        }
    }

    /**
     * Handles the selection and usage of a targeting scope
     * @param player The player shooting
     * @param victim The target of the player
     */
    public void useTargetingscope(Player player, Player victim){
        List<PowerUp> scopes = player.getPowerUps().stream()
                .filter(pwu -> pwu.getType().equals(PowerUpType.TARGETING_SCOPE))
                .filter(pwu -> player.canPay(Arrays.asList(pwu.getAmmo())))
                .collect(Collectors.toList());
        RemotePlayer remotePlayer = remoteUsers.get(player.getNickname());
        try{
            if(!scopes.isEmpty()){
                List<PowerUp> selected = remotePlayer.selectIdentifiable(scopes, 0, 1, Dialog.TARGETING_SCOPE_SELECT);
                if(!selected.isEmpty()){
                    player.removePowerUp(selected.get(0));
                    player.addDrawnPowerUp(selected.get(0));
                    // In case the player can pay the targeting scope using solely THAT targeting scope
                    if(!player.canPay(Arrays.asList(selected.get(0).getAmmo()))){
                        player.addPowerUp(selected.get(0));
                        player.clearDrawnPowerUps();
                        return;
                    }
                    paymentGateway.payCost(Arrays.asList(selected.get(0).getAmmo()), player, remotePlayer);
                    powerUpController.activatePowerUp(selected.get(0), player.getNickname(), victim);
                    player.clearDrawnPowerUps();
                    match.getGameBoard().getPowerUpDeck().discard(selected.get(0));
                    alreadyScoped.add(victim);
                }
            }
        } catch(RemoteException e){
            // Shit happens, don't worry, player will be disconnected next action
            LOG.log(Level.INFO,"Player {0} disconnected while using targeting scope", player.getNickname());
        }

    }
}