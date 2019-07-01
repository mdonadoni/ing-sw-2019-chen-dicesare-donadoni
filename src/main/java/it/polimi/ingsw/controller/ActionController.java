package it.polimi.ingsw.controller;

import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.weapons.Weapon;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Controller class that manages an Action
 */
public class ActionController {
    private static final Logger LOG = Logger.getLogger(ActionController.class.getName());

    private Match match;
    private Map<String, RemotePlayer> remoteUsers;
    private PowerUpController powerUpController;
    private PaymentGateway paymentGateway;
    private Updater updater;

    /**
     * Standard constructor
     * @param match the match currently going on
     * @param remoteUsers a map containing all the RemotePlayer references
     */
    public ActionController(Match match, Map<String, RemotePlayer> remoteUsers, Updater updater){
        this.match = match;
        this.remoteUsers = remoteUsers;
        this.powerUpController = new PowerUpController(match, remoteUsers);
        this.paymentGateway = new PaymentGateway(match);
        this.updater = updater;
    }

    /**
     * Does everything needed to fully perform an action
     * @param player the player acting
     * @param action the complete action
     * @throws RemoteException in case something goes wrong
     */
    public void performAction(Player player, Action action) throws RemoteException{
        // Now cycle through all the basic actions, collecting together the movement actions
        Iterator<BasicAction> iter = action.getActions().iterator();
        BasicAction basic;
        String playerName = player.getNickname();
        int nMov = 0;
        while(iter.hasNext()){
            basic = iter.next();
            if(basic == BasicAction.MOVEMENT){ // Count how many basic movements I have, so that I can do them all at once
                nMov++;
            }
            else{
                if(nMov>0){ // At the first non-movement should handle the collected movements
                    handleMovement(playerName, nMov);
                }
                // Then handle the current basic action
                handleBasicAction(playerName, basic);
                nMov = 0; // In any case I want to reset the nMov counter
            }
        }
        if(nMov>0)
            handleMovement(playerName, nMov);

        // Everyone should update now the model
        updater.updateModelToEveryone();
    }

    /**
     * Handles a single basic action
     * @param playerName the player acting
     * @param basic the basic action type
     * @throws RemoteException in case something goes wrong
     */
    private void handleBasicAction(String playerName, BasicAction basic) throws RemoteException{
        switch(basic){
            case MOVEMENT:
                handleMovement(playerName, 1);
                break;
            case RELOAD:
                handleReload(playerName);
                break;
            case GRAB:
                handleGrab(playerName);
                break;
            case SHOOT:
                handleShoot(playerName);
                break;
            case SKIP:
                handleSkip(playerName);
                break;
            case POWERUP:
                handleUsePowerUp(playerName);
        }

    }

    /**
     * Manages a Movement Action, moving around the player
     * @param playerName the player acting
     * @param nMov how far the player can go
     * @throws RemoteException in case something goes wrong
     */
    private void handleMovement(String playerName, int nMov) throws RemoteException{
        Player player = match.getPlayerByNickname(playerName);
        RemotePlayer remotePlayer = remoteUsers.get(playerName);
        Square selectedSquare;
        // Build the list of the squares available for the movement
        List<Square> whereToMove = player.getSquare().getSquaresByDistance(nMov);
        // Ask the player where to move
        selectedSquare = remotePlayer.selectIdentifiable(whereToMove, 1, 1, Dialog.MOVE).get(0);
        // Apply the movement
        player.move(selectedSquare);

        updater.updateModel(playerName);
    }

    /**
     * Handles the recharge routine for a player
     * @param playerName the player acting
     * @throws RemoteException in case something goes wrong
     */
    public void handleReload(String playerName) throws RemoteException {
        Player player = match.getPlayerByNickname(playerName);
        RemotePlayer remotePlayer = remoteUsers.get(playerName);

        LOG.log(Level.INFO, "Reload routine started");

        // Cycle through all the player weapons
        for(Weapon currentWeapon : player.getWeapons()){
            if(player.canPay(currentWeapon.getTotalRechargeCost()) && !currentWeapon.isCharged()){
                // Ask the user if he wants to recharge this weapon
                List<Weapon> selectingWeapon = new ArrayList<>();
                selectingWeapon.add(currentWeapon);
                if(remotePlayer.selectIdentifiable(selectingWeapon, 0, 1, Dialog.RELOAD).size() == 1){
                    // Pay the recharge cost
                    paymentGateway.payCost(currentWeapon.getTotalRechargeCost(), player, remotePlayer);
                    // Set the recharge flag
                    currentWeapon.setCharged(true);
                }
            }
        }

        updater.updateModel(playerName);
    }

    /**
     * The player performs the grab action, if he's on a StandardSquare, automatically gets the ammotile, otherwise
     * asks the user which weapon he wants to grab and handles possible difficult situations
     * @param playerName the player who's acting
     * @throws RemoteException in case something goes wrong
     */
    private void handleGrab(String playerName)  throws RemoteException{
        Player player = match.getPlayerByNickname(playerName);
        RemotePlayer remotePlayer = remoteUsers.get(playerName);
        Board board = match.getGameBoard().getBoard();

        // Check if the player is on a standard square
        if(board.isStandardSquare(player.getSquare().getCoordinates())){
            StandardSquare sq = (StandardSquare) player.getSquare();
            // Automatically grab the ammoTile, if there's one
            if(sq.hasAmmoTile()){
                AmmoTile tile = sq.getAmmoTile();
                player.grabAmmo(tile);
                if(tile.hasPowerUp()){ // In case the Ammotile has a powerup
                    drawPowerUp(player, remotePlayer);
                }
                match.getGameBoard().getAmmoTileDeck().discard(tile); // Discard the tile
                sq.removeAmmoTile();
            }
        }
        else{ // If the player is on a SpawnPoint, I must ask him what weapon he wants to grab
            SpawnPoint spw = (SpawnPoint) player.getSquare();
            List<Weapon> weapons = spw.getWeapons();
            // Filter the weapons so that I prompt the user only those who can be grabbed
            List<Weapon> grabbableWeapons = weapons.stream()
                    .filter(e -> player.canPay(e.getPickupColor()))
                    .collect(Collectors.toList());
            if(!grabbableWeapons.isEmpty()){
                Weapon selectedWeapon = remotePlayer.selectIdentifiable(grabbableWeapons, 1, 1, Dialog.WEAPON_GRAB).get(0);
                // Grab the weapon
                // In this city you pay before, then you get the camels
                paymentGateway.payCost(selectedWeapon.getPickupColor(), player, remotePlayer);
                if(player.canGrabWeapon()){
                    player.grabWeaponFromGround(selectedWeapon);
                }
                else
                    discardAndGrabWeapon(selectedWeapon, player);
            }
        }

        updater.updateModel(playerName);
    }

    /**
     * Dummy method, will be implemented later
     */
    private void handleShoot(String playerName) throws RemoteException{
        Player player = match.getPlayerByNickname(playerName);
        RemotePlayer remotePlayer = remoteUsers.get(playerName);
        List<Player> otherPlayers = match.getOtherPlayers(playerName);

        Player victim = remotePlayer.selectIdentifiable(otherPlayers, 1, 1, Dialog.SHOOT_TARGET).get(0);

        victim.takeDamage(player.getColor(), 2);
        victim.addMark(player.getColor(), 1);

        updater.updateModel(playerName);
    }

    /**
     * Just notifies the user that he skipped his turn
     * @param playerName the player I want to notify
     */
    private void handleSkip(String playerName) {
        RemotePlayer remotePlayer = remoteUsers.get(playerName);
        remotePlayer.safeShowMessage(Dialog.SKIP);
    }

    /**
     * Makes the player discard a weapon and then adds the weapon that didn't fit before
     * @param toBeGrabbed the weapon he couldn't grab
     * @param player the player who's acting
     * @throws RemoteException in case something goes wrong
     */
    private void discardAndGrabWeapon(Weapon toBeGrabbed, Player player) throws RemoteException{
        List<Weapon> weapons = new ArrayList<>(player.getWeapons());
        RemotePlayer remote = remoteUsers.get(player.getNickname());
        SpawnPoint spw = (SpawnPoint) player.getSquare();

        // Select which weapon to discard
        Weapon selectedWeapon = remote.selectIdentifiable(weapons, 1, 1, Dialog.DISCARD_WEAPON).get(0);
        player.removeWeapon(selectedWeapon);
        selectedWeapon.setCharged(true);

        // Make room in the square for the discarded weapon
        spw.removeWeapon(toBeGrabbed);
        // Place down the weapon
        spw.addWeapon(selectedWeapon);
        // Adds the weapon to the player
        player.grabWeapon(toBeGrabbed);

        updater.updateModel(player.getNickname());
    }

    /**
     * Makes a player use a PowerUp he owns
     * @param playerName The player acting
     * @throws RemoteException In case something goes wrong
     */
    private void handleUsePowerUp(String playerName) throws RemoteException {
        Player player = match.getPlayerByNickname(playerName);
        RemotePlayer remotePlayer = remoteUsers.get(playerName);
        List<PowerUp> availablePowerups = player.getActivablePowerups();
        List<Player> enemies = match.getOtherPlayers(playerName);

        // Ask the user which powerup he wants to use
        List<PowerUp> selection = remotePlayer.selectIdentifiable(availablePowerups, 0, 1, Dialog.SELECT_POWERUP);

        if(!selection.isEmpty()){
            PowerUp selectedPowerup = selection.get(0);
            // Activate the PowerUp, due to the game ruling, the selectedPowerUp can be only a TELEPORTER or a NEWTON
            if(selectedPowerup.getType().equals(PowerUpType.TELEPORTER))
                powerUpController.activatePowerUp(selectedPowerup, playerName);
            else if(selectedPowerup.getType().equals(PowerUpType.NEWTON)){
                List<Player> selectableEnemies = enemies.stream()
                        .filter(en -> en.getSquare()!=null)
                        .collect(Collectors.toList());
                Player targetPlayer = remotePlayer.selectIdentifiable(selectableEnemies, 0, 1, Dialog.TARGET_PLAYER).get(0);
                powerUpController.activatePowerUp(selectedPowerup, playerName, targetPlayer);
            }

            player.removePowerUp(selectedPowerup);
            match.getGameBoard().getPowerUpDeck().discard(selectedPowerup);
        }

        updater.updateModel(playerName);
    }

    /**
     * A player draws a PowerUp, discards one if necessary
     * @param player The player acting
     * @param remotePlayer The RemotePlayer object that refers to the player
     * @throws RemoteException In case something goes wrong
     */
    private void drawPowerUp(Player player, RemotePlayer remotePlayer) throws RemoteException{
        // Get the PowerUp from the deck
        PowerUp toBeDrawn = match.getGameBoard().getPowerUpDeck().draw();

        // Check if it needs to discard one
        if(player.canAddPowerUp()){
            player.addPowerUp(toBeDrawn); // That's it
        }
        else{
            player.addDrawnPowerUp(toBeDrawn);
            updater.updateModel(player.getNickname());
            // Now the player has to select which powerup it wants to discard
            List <PowerUp> tempList = new ArrayList<>();
            tempList.addAll(player.getPowerUps());
            tempList.addAll(player.getDrawnPowerUps());
            PowerUp selectedPwu = remotePlayer.selectIdentifiable(tempList, 1, 1, Dialog.DISCARD_POWERUP).get(0);
            // Clear up the things
            player.removePowerUp(selectedPwu);
            match.getGameBoard().getPowerUpDeck().discard(selectedPwu);
            if(!toBeDrawn.equals(selectedPwu))
                player.addPowerUp(toBeDrawn);
            player.clearDrawnPowerUps(); // Clear the player's hand
        }

        updater.updateModel(player.getNickname());
    }
}
