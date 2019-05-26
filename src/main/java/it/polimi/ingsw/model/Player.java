package it.polimi.ingsw.model;

import it.polimi.ingsw.model.weapons.Weapon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represent a Player of the game.
 */
public class Player extends Identifiable{
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
    private int points;
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
     * Keeps the power-ups drawn by a player
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
     * first player of the match
     */
    private boolean beforeFistPlayerFF;

    /**
     * Constructor of Player
     * @param nickname nickname of the player.
     */
    public Player(String nickname, PlayerToken color){
        this.nickname = nickname;
        this.color = color;
        startingPlayer = false;
        skulls = 0;
        active=true;
        points = 0;
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
     * @param startingPlayer True if the player starts the match, false otherwise
     */
    public void setStartingPlayer(boolean startingPlayer) {
        this.startingPlayer = startingPlayer;
    }

    /**
     * Flip the player's board
     */
    public void flipBoard() {
        this.boardFlipped = !boardFlipped;
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
        return damageTaken.size()>=11;
    }

    /**
     * Get the list of the damage taken by the player.
     * @return the list of PlayerToken as the damage taken.
     */
    public List<PlayerToken> getDamageOrder(){
        return damageTaken;
    }

    /**
     * Get the first player token taken by the player
     * @return the first PlayerToken on this player.
     */
    public PlayerToken getFirstBlood(){
        return damageTaken.get(0);
    }

    /**
     * Get the player token that inflicted the kill damage.
     * @return the PlayerToken that killed the player.
     */
    public PlayerToken getKillshot(){
        return damageTaken.get(10);
    }

    /**
     * Get the player token that did the last damage after the kill shot.
     * @return the last PlayerToken of damage taken by the player.
     */
    public PlayerToken getOverkill(){
        return damageTaken.get(11);
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
        return damageTaken;
    }

    /**
     * Damage the player
     * @param token the PlayerToken that inflict the damage
     * @param n the number of damage inflicted
     */
    public void takeDamage(PlayerToken token, int n){
        PlayerToken p;
        for(int i=0; i<n && damageTaken.size()<12; i++){
            for (Iterator<PlayerToken> it = marks.iterator(); it.hasNext(); ) {
                p = it.next();
                if(p==token){
                    damageTaken.add(token);
                    it.remove();
                }
            }
            damageTaken.add(token);
        }
    }

    /**
     * Remove all the damage from a player.
     */
    public void resetDamage(){
        damageTaken.clear();
    }

    /**
     * Add PlayerToken as mark
     * @param token the PlayerToken that marked
     * @param n the number of marks
     */
    public void addMark(PlayerToken token, int n){
        for(int i=0; i<n && countMarks(token)<3; i++) {
            marks.add(token);
        }
    }

    /**
     * Count how many PlayerToken of the same type the player has
     * @param token the type of PlayerToken to count
     * @return the number of same PlayerToken
     */
    public int countMarks(PlayerToken token){
        int counter=0;
        for( PlayerToken p : marks){
            if(p==token)
                counter++;
        }
        return counter;
    }

    /**
     * Add an AmmoTile's ammo to the player's reserve
     * @param ammoTile the AmmoTile grabbed by the player
     */
    public void grabAmmo(AmmoTile ammoTile){
        for (AmmoColor c : ammoTile.getAmmo()) {
            addAmmo(c);
        }
    }

    /**
     * Count the ammo with the same color
     * @param ammoColor the ammo color to count
     * @return the number of ammo
     */
    public int countAmmo(AmmoColor ammoColor){
        int counter=0;
        for( AmmoColor a : ammo){
            if(a==ammoColor)
                counter++;
        }
        return counter;
    }

    /**
     * Grab a weapon from the spawn point where the player is.
     * @param weapon the weapon to grab.
     */
    public void grabWeapon(Weapon weapon) {
        if (weapons.size() < 3) {
            weapons.add(weapon);
        }
    }

    /**
     * Get the list of weapon.
     * @return the list of weapon.
     */
    public List<Weapon> getWeapons() {
        return weapons;
    }

    /**
     * Increment the number of skull
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
     * Get the number of skulls
     * @return number of skulls
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
     * Gets the player's status
     * @return the player's status
     */
    public boolean isActive(){
        return active;
    }

    /**
     * Add points to the player.
     * @param points gained points by the player.
     */
    public void addPoints(int points){
        this.points+=points;
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
     * Remove a certain amount of ammo from the player
     * @param ammoColor the color of the ammo to remove.
     * @param n the amount of ammo to remove.
     *
     */
    public void removeAmmo(AmmoColor ammoColor, int n){
        if(countAmmo(ammoColor)<n) {
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
        if(powerUps.size() < 3)
            powerUps.add(pwu);
        else
            throw new InvalidOperationException("Player "+ nickname + " already has 3 Power-Ups!");
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
     * Removes a Power-Up from the drawnPowerUps list
     * @param pwu
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
    public int getPoints() {
        return points;
    }

    /**
     * Moves the player to another Square
     * @param destinationSquare The Square where the player should be moved
     */
    public void move(Square destinationSquare){
        if (square != null)
            square.removePlayer(this);
        square = destinationSquare;
        destinationSquare.addPlayer(this);
    }

    /**
     * Adds a single instance of damage, useful for when you don't want to trigger marks due to
     * weird ruling
     * @param damageColor the color of the damage
     */
    public void addDamage(PlayerToken damageColor, int numberOfDamage){
        for(int i = 0; i<numberOfDamage; i++)
            damageTaken.add(damageColor);
    }

    /**
     * Adds a single ammo to the player
     * @param ammo the ammo to be added
     */
    public void addAmmo(AmmoColor ammo){
        if(countAmmo(ammo) < 3)
            this.ammo.add(ammo);
    }

    public void setBeforeFistPlayerFF(Boolean val){
        beforeFistPlayerFF = val;
    }

    public Boolean getBeforeFirstPlayerFF(){
        return beforeFistPlayerFF;
    }
}
