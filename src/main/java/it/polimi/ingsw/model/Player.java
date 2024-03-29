package it.polimi.ingsw.model;

import it.polimi.ingsw.model.weapons.Weapon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represent a Player of the game.
 */
public class Player extends Identifiable{
    /**
     * It indicates the maximum amount of power-ups that any player can hold.
     */
    private static final int MAX_POWERUP_HAND = 3;
    /**
     * It indicates the maximum amount of weapon that any player can hold.
     */
    private static final int MAX_WEAPON_HAND = 3;
    /**
     * It indicates the maximum amount of damage that any player can take.
     */
    private static final int MAX_DAMAGE = 12;
    /**
     * It indicates the index of the lethal damage.
     */
    private static final int LETHAL_DAMAGE_INDEX = MAX_DAMAGE-2;
    /**
     * It indicates the index of the overkill damage.
     */
    private static final int OVERKILL_DAMAGE_INDEX = MAX_DAMAGE-1;
    /**
     * It indicates the index of the first blood damage.
     */
    private static final int FIRST_BLOOD_INDEX = 0;
    /**
     * It indicates the maximum amount of marks per player on any player
     */
    private static final int MAX_MARKS_PER_PLAYER = 3;

    /**
     * It's the nickname of the player identify the Player.
     */
    private String nickname;
    /**
     * It shows if a player is the player who starts the game.
     */
    private boolean startingPlayer;
    /**
     * It tracks how many times the player has died.
     */
    private int skulls;
    /**
     * It shows if the player is active.
     */
    private boolean active;
    /**
     * It keeps how many points the Player has.
     */
    private int totalPoints;
    /**
     * It keeps the number of points given from the killshot track. Useful for
     * final standings calculation.
     */
    private int killShotTrackPoints;
    /**
     * It shows if the player board has been flipped.
     */
    private boolean boardFlipped;
    /**
     * It represent the player color.
     */
    private PlayerToken color;
    /**
     * It keeps track of the marks of other players on a player.
     */
    private List<PlayerToken> marks;
    /**
     * It keeps track of damage taken by a player from other players.
     */
    private List<PlayerToken> damageTaken;
    /**
     * It keeps the weapons held by a player.
     */
    private List<Weapon> weapons;
    /**
     * It keeps the power-up held by a player.
     */
    private List<PowerUp> powerUps;

    /**
     * Keeps the power-ups drawn by a player.
     */
    private List<PowerUp> drawnPowerUps;

    /**
     * It keeps track of the ammo available of a player.
     */
    private List<AmmoColor> ammo;
    /**
     * It shows in which square the player is.
     */
    private Square square;

    /**
     * When Final Frenzy starts, some actions can be performed only by players who play their FF turn before the
     * first player of the match.
     */
    private boolean beforeFistPlayerFF;

    /**
     * Constructor of Player.
     * @param nickname nickname of the player.
     * @param color the color of the player.
     */
    public Player(String nickname, PlayerToken color){
        this.nickname = nickname;
        this.color = color;
        startingPlayer = false;
        skulls = 0;
        active=true;
        totalPoints = 0;
        killShotTrackPoints = 0;
        boardFlipped = false;
        marks = new ArrayList<>();
        damageTaken = new ArrayList<>();
        weapons = new ArrayList<>();
        powerUps = new ArrayList<>();
        drawnPowerUps = new ArrayList<>();
        ammo = new ArrayList<>();
    }

    /**
     * Get the square where the player is.
     * @return the square where the player is.
     */
    public Square getSquare(){return square;}

    /**
     * Set the player position on another square.
     * @param square the square where to move the player.
     */
    public void setSquare(Square square){
        this.square = square;
    }

    /**
     * Change player starting player status.
     * @param startingPlayer True if the player starts the match, false otherwise.
     */
    public void setStartingPlayer(boolean startingPlayer) {
        this.startingPlayer = startingPlayer;
    }

    /**
     * Flip the player's board.
     */
    public void flipBoard() {
        this.boardFlipped = true;
        skulls = 0;
    }

    /**
     * Get if board is flipped.
     * @return True if board is flipped, false otherwise.
     */
    public boolean isBoardFlipped() {
        return boardFlipped;
    }

    /**
     * Show if the player is dead.
     * @return true if the player has taken at least 11 damage, false otherwise.
     */
    public boolean isDead(){
        return damageTaken.size() > LETHAL_DAMAGE_INDEX;
    }

    /**
     * The damage order of this player's board. It is used to calculate points assignment.
     * @return A list containing the Tokens of the players that dealt damage to this player, ordered by the amount of
     *         damage dealt and secondly by the recentness the damage.
     */
    public List<PlayerToken> getDamageOrder(){
        return Util.uniqueStableSortByCount(damageTaken);
    }

    /**
     * Get the first player token taken by the player.
     * @return the first PlayerToken on this player.
     */
    public PlayerToken getFirstBlood(){
        if (FIRST_BLOOD_INDEX < damageTaken.size()) {
            return damageTaken.get(FIRST_BLOOD_INDEX);
        } else {
            return null;
        }
    }

    /**
     * Return the damage that killed the player.
     * @return The PlayerToken that killed the player.
     */
    public PlayerToken getLethalDamage(){
        if (LETHAL_DAMAGE_INDEX < damageTaken.size()) {
            return damageTaken.get(LETHAL_DAMAGE_INDEX);
        } else {
            return null;
        }
    }

    /**
     * Get the player token that did the last damage after the kill shot.
     * @return the last PlayerToken of damage taken by the player.
     */
    public PlayerToken getOverkill(){
        if(OVERKILL_DAMAGE_INDEX < damageTaken.size()) {
            return damageTaken.get(OVERKILL_DAMAGE_INDEX);
        } else {
            return null;
        }
    }

    /**
     * Get player color.
     * @return Color of the player.
     */
    public PlayerToken getColor() {
        return color;
    }

    /**
     * Get the damage streak of the player.
     * @return List of damage streak.
     */
    public List<PlayerToken> getDamageTaken() {
        return new ArrayList<>(damageTaken);
    }

    /**
     * Damage the player with a certain amount of
     * @param token The PlayerToken that inflict the damage
     * @param n The amount of damage inflicted
     */
    public void takeDamage(PlayerToken token, int n){
        n += countMarks(token);
        marks.removeIf(p -> p == token);
        while (n > 0 && damageTaken.size() < MAX_DAMAGE) {
            damageTaken.add(token);
            n--;
        }
    }

    /**
     * Remove all the damage from a player.
     */
    public void resetDamage(){
        damageTaken.clear();
    }

    /**
     * Add PlayerToken as mark.
     * @param token The PlayerToken that marks the player
     * @param n The amount of marks.
     */
    public void addMark(PlayerToken token, int n){
        n = Math.min(n, MAX_MARKS_PER_PLAYER - countMarks(token));
        while (n > 0) {
            marks.add(token);
            n--;
        }
    }

    /**
     * Count how many PlayerToken of the same type the player has.
     * @param token The type of PlayerToken to count.
     * @return The amount of same PlayerToken.
     */
    public int countMarks(PlayerToken token){
        return Collections.frequency(marks, token);
    }

    /**
     * Add an AmmoTile's ammo to the player's reserve.
     * @param ammoTile The AmmoTile grabbed by the player.
     */
    public void grabAmmo(AmmoTile ammoTile){
        for (AmmoColor c : ammoTile.getAmmo()) {
            addAmmo(c);
        }
    }

    /**
     * Count the ammo with the same color.
     * @param ammoColor The ammo color to count.
     * @return The number of ammo.
     */
    public int countAmmo(AmmoColor ammoColor){
        return Collections.frequency(ammo, ammoColor);
    }

    /**
     * Grab a weapon from the spawn point where the player is.
     * @param weapon The weapon to grab.
     */
    public void grabWeapon(Weapon weapon) {
        if (canGrabWeapon()) {
            weapons.add(weapon);
        }
    }

    /**
     * Adds the weapon to the player and removes the weapon from the square.
     * @param weapon The weapon to grab from a spawn point.
     */
    public void grabWeaponFromGround(Weapon weapon){
        try{
            SpawnPoint spwSquare = (SpawnPoint) this.getSquare();
            if(!spwSquare.getWeapons().contains(weapon))
                throw new InvalidOperationException("There's no such weapon on this square");
            if (canGrabWeapon()){
                weapons.add(weapon);
                spwSquare.removeWeapon(weapon);
            }
            else
                throw new FullInventoryException();
        } catch (ClassCastException e){
            throw new InvalidOperationException("You cannot grab a weapon if you are on a StandardSquare!");
        }
    }

    /**
     * This method return if the player can grab a weapon.
     * @return True if the player holds less than MAX_WEAPON_HAND weapon, false otherwise.
     */
    public boolean canGrabWeapon(){
        return weapons.size() < MAX_WEAPON_HAND;
    }

    /**
     * Get the list of weapon.
     * @return the list of weapon.
     */
    public List<Weapon> getWeapons() {
        return new ArrayList<>(weapons);
    }

    /**
     * Remove a weapon from the player.
     * @param weapon The weapon to remove from the player.
     */
    public void removeWeapon(Weapon weapon){
        this.weapons.remove(weapon);
    }

    /**
     * Increment the number of skull.
     */
    public void addSkull(){
        skulls++;
    }

    /**
     * Set the number of skulls.
     * @param skulls number of skulls.
     */
    public void setSkulls(int skulls) {
        this.skulls = skulls;
    }

    /**
     * Get the number of skulls.
     * @return number of skulls.
     */
    public int getSkulls(){
        return skulls;
    }

    /**
     * Set the player's status.
     * @param active the player's status to set.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the player's status.
     * @return True if the player is active, false otherwise.
     */
    public boolean isActive(){
        return active;
    }

    /**
     * Add points to the player.
     * @param points gained points by the player.
     */
    public void addPoints(int points){
        this.totalPoints += points;
    }

    /**
     * Add points from the killshot track to killShotTrackPoints and to totalPoints.
     * @param points Number of points to be added.
     */
    public void addKillShotTrackPoints(int points) {
        this.killShotTrackPoints += points;
        addPoints(points);
    }

    /**
     * Check if the player is the first to start.
     * @return true if the player start first, false otherwise.
     */
    public boolean isStartingPlayer() {
        return startingPlayer;
    }

    /**
     * Get the list of marks on the player.
     * @return the list of the marks.
     */
    public List<PlayerToken> getMarks() {
        return marks;
    }

    /**
     * Get the list of ammo available.
     * @return the list of ammo available.
     */
    public List<AmmoColor> getAmmo() {
        return ammo;
    }

    /**
     * Remove a certain amount of ammo from the player.
     * @param ammoColor the color of the ammo to remove.
     * @param n the amount of ammo to remove.
     *
     */
    public void removeAmmo(AmmoColor ammoColor, int n){
        if(countAmmo(ammoColor) < n) {
            throw new InvalidOperationException("The player doesn't have enough ammo");
        }
        AmmoColor a;
        int i = 0;
        for (Iterator<AmmoColor> t = ammo.iterator(); t.hasNext() && i < n; ) {
            a = t.next();
            if (a == ammoColor) {
                t.remove();
                i++;
            }
        }
    }

    /**
     * Adds a PowerUp to the player's hand
     * @param pwu the PowerUp to be added
     */
    public void addPowerUp(PowerUp pwu){
        if(powerUps.size() < MAX_POWERUP_HAND)
            powerUps.add(pwu);
        else
            throw new InvalidOperationException("Player "+ nickname + " already has"+ MAX_POWERUP_HAND +"Power-Ups!");
    }

    /**
     * This method return if the player can hold more power-up
     * @return True if the player has less than MAX_POWERUP_HAND
     */
    public boolean canAddPowerUp(){
        return powerUps.size() < MAX_POWERUP_HAND;
    }

    /**
     * Removes a certain PowerUp from the player's hand
     * @param pwu the PowerUp to be removed
     */
    public void discardPowerUp(PowerUp pwu){
        powerUps.remove(pwu);
    }

    /**
     * Getter for the list of PowerUps
     * @return The list of PowerUps
     */
    public List<PowerUp> getPowerUps(){
        return new ArrayList<>(powerUps);
    }

    /**
     * Getter for the list of drawnPowerUps
     * @return The list of drawnPowerUps
     */
    public List<PowerUp> getDrawnPowerUps(){
        return new ArrayList<>(drawnPowerUps);
    }

    /**
     * Adds a Power-Up to the drawnPowerUps list
     * @param pwu the drawn Power-Up
     */
    public void addDrawnPowerUp (PowerUp pwu){
        drawnPowerUps.add(pwu);
    }

    /**
     * Clears the drawnPowerUps list
     */
    public void clearDrawnPowerUps(){
        drawnPowerUps.clear();
    }

    /**
     * Removes a PowerUp from the drawnPowerUps list.
     * @param pwu The power-up to remove from the player.
     */
    public void removeDrawnPowerUp(PowerUp pwu){
        drawnPowerUps.remove(pwu);
    }

    /**
     * Get player's nickname.
     * @return Player nickname.
     */
    public String getNickname(){
        return nickname;
    }

    /**
     * Get player's points
     * @return player's points
     */
    public int getTotalPoints() {
        return totalPoints;
    }

    /**
     * Get player's killshot track points.
     * @return Player's killshot track points.
     */
    public int getKillShotTrackPoints() {
        return killShotTrackPoints;
    }

    /**
     * Moves the player to an another Square.
     * @param destinationSquare The Square where the player is moved to.
     */
    public void move(Square destinationSquare){
        if (square != null)
            square.removePlayer(this);
        square = destinationSquare;
        destinationSquare.addPlayer(this);
    }

    /**
     * Adds a single instance of damage, useful for when you don't want to trigger marks due to
     * weird ruling.
     * @param damageColor The color of the damage.
     * @param numberOfDamage The amount of damage.
     */
    public void addDamageWithoutMarks(PlayerToken damageColor, int numberOfDamage){
        for(int i = 0; i < numberOfDamage && damageTaken.size() < MAX_DAMAGE; i++) {
            damageTaken.add(damageColor);
        }
    }

    /**
     * Adds a single ammo to the player
     * @param ammo the ammo to be added
     */
    public void addAmmo(AmmoColor ammo){
        if(countAmmo(ammo) < 3)
            this.ammo.add(ammo);
    }

    /**
     *  Set the possibility of the player to perform final frenzy moves
     * @param val boolean parameter
     */
    public void setBeforeFistPlayerFF(Boolean val){
        beforeFistPlayerFF = val;
    }

    /**
     *  Return if the player comes before the first player
     * @return True if the player turn is before the first player, false otherwise
     */
    public Boolean getBeforeFirstPlayerFF(){
        return beforeFistPlayerFF;
    }

    /**
     * This method returns all the actions a player can perform
     * @param ffActive whether the Final Frenzy is active
     * @return An ArrayList containing all the actions that a player can perform
     */
    public List<Action> supplyActions(boolean ffActive){
        List<Action> actions;
        List<Action> availableActions = new ArrayList<>();

        actions = ActionSupplier.getInstance().getActions();
        for(Action act : actions){
            if(act.canPerform(this, ffActive))
                availableActions.add(act);
        }

        if (getActivablePowerups().isEmpty()) {
            availableActions = availableActions.stream()
                    .filter(act -> !act.getActions().contains(BasicAction.POWERUP))
                    .collect(Collectors.toList());
        }
        if(!canShoot()){
            availableActions = availableActions.stream()
                    .filter(act -> !act.getActions().contains(BasicAction.SHOOT))
                    .collect(Collectors.toList());
        }

        return availableActions;
    }

    /**
     * Return if the player can shoot
     * @return True if the player can shoot, false otherwise
     */
    private boolean canShoot(){
        boolean result = false;

        for(Weapon wp : weapons){
            if(wp.isCharged())
                result = true;
        }

        return result;
    }

    /**
     * States whether a player can pay a certain cost. First checks if you can pay using solely the ammos, then if you
     * can't, checks also the powerups.
     * @param cost List of AmmoColors that represents the cost of the thing you have to pay for
     * @return True if you can pay, false if you are too poor to pay
     */
    public boolean canPay(List<AmmoColor> cost){
        List<AmmoColor> ammoCost = new ArrayList<>(cost);
        // Firstly runs through all the available ammo
        for(AmmoColor singleAmmo : this.ammo){
            ammoCost.remove(singleAmmo);
        }
        // If the cost has been "payed", we can state that we are able to pay the cost
        if(ammoCost.isEmpty())
            return true;
        // Otherwise must check also the powerups
        else{
            for (PowerUp pwu : this.powerUps){
                ammoCost.remove(pwu.getAmmo());
            }
        }
        return ammoCost.isEmpty();
    }

    /**
     * Remove a power-up from the player
     * @param pwu The power-up to remove
     */
    public void removePowerUp(PowerUp pwu){
        powerUps.remove(pwu);
    }

    /**
     * @return a list containing all the player's powerups that are usable in any situation
     */
    public List<PowerUp> getActivablePowerups(){
        return powerUps.stream()
                .filter(e -> e.getType().equals(PowerUpType.NEWTON) || e.getType().equals(PowerUpType.TELEPORTER))
                .collect(Collectors.toList());
    }

    /**
     * Remove the player from the board
     */
    public void removeFromBoard(){
        if(square != null){
            square.removePlayer(this);
            square = null;
        }
    }

    /**
     * Return if the player has a tag back
     * @return True if the player has a tag back, false otherwise
     */
    public boolean hasTagback(){
        return powerUps.stream()
                .anyMatch(pwu -> pwu.getType().equals(PowerUpType.TAGBACK_GRANADE));
    }

    /**
     * Return if the player has a targeting scope
     * @return True if the player has a targeting scope, false otherwise
     */
    public boolean hasTargetingscope(){
        return powerUps.stream()
                .anyMatch(pwu -> pwu.getType().equals(PowerUpType.TARGETING_SCOPE));
    }
}
