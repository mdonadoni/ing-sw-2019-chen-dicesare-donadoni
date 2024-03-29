package it.polimi.ingsw.model;

import it.polimi.ingsw.model.weapons.Weapon;

import java.util.ArrayList;
import java.util.List;


/**
 * Class representing the GameBoard.
 */
public class GameBoard {
    /**
     * Number of initial skulls for this gameboard.
     */
    private int initialSkullNumber;
    /**
     * Number of skulls left on the gameboard.
     */
    private int remainingSkulls;
    /**
     * Keeps track of the kills scored by the players, every kill might be a multiple one, so we need a list of lists.
     */
    private List<List<PlayerToken>> killShotTrack;
    /**
     * A deck containing the weapon cards.
     */
    Deck<Weapon> weaponDeck;
    /**
     * A deck containing the powerup cards.
     */
    Deck<PowerUp> powerUpDeck;
    /**
     * A deck containing thr ammo tiles.
     */
    Deck<AmmoTile> ammoTileDeck;

    /**
     * Board with square and spawnpoints.
     */
    Board board;

    /**
     * Standard constructor.
     */
    GameBoard() {
    }

    /**
     * Constructor that allows to specify the number of skulls
     * @param skulls number of skulls to be placed on the gameboard
     * @param factory the factory of the model
     */
    public GameBoard(int skulls, ModelFactory factory){
        remainingSkulls = skulls;
        initialSkullNumber = skulls;
        killShotTrack = new ArrayList<>();
        weaponDeck = factory.createWeaponDeck();
        powerUpDeck = factory.createPowerUpDeck();
        ammoTileDeck = factory.createAmmoTileDeck();
        board = factory.createBoard();
        refillAmmoTile();
        refillSpawnPoints();
    }

    /**
     * Goes through all the StandardSquares and if the ammoTile is missing, adds one.
     */
    public void refillAmmoTile() {
        for(StandardSquare sq : board.getStandardSquares()){
            if(!sq.hasAmmoTile() && ammoTileDeck.canDraw()){
                sq.setAmmoTile(ammoTileDeck.draw());
            }
        }
    }

    /**
     * Goes through all the SpawnPoints and if it's not full of weapon, adds until full.
     */
    public void refillSpawnPoints() {
        for(SpawnPoint spw : board.getSpawnPoints()){
            while(!spw.isFull() && weaponDeck.canDraw())
                spw.addWeapon(weaponDeck.draw());
        }
    }

    /**
     * Add a kill to the killShotTrack, this method also decreases the number of skulls left. According to the rules, if
     * there are no skulls left, all the kills are appended to the last kill scored.
     * @param tokens The tokens of the player who scored the kill.
     */
    public void addKill(List<PlayerToken> tokens){
        if(remainingSkulls > 0){
            remainingSkulls--;
            killShotTrack.add(tokens);
        }
        else
        {
            if (initialSkullNumber == killShotTrack.size())
                killShotTrack.add(new ArrayList<PlayerToken>());

            killShotTrack.get(initialSkullNumber).addAll(tokens);
        }
    }

    /**
     * Return the amount of remaining skulls.
     * @return The amount of remaining skulls.
     */
    public int getRemainingSkulls(){
        return remainingSkulls;
    }

    /**
     * Return the kill shot track.
     * @return The kill shot track.
     */
    public List<List<PlayerToken>> getKillShotTrack(){
        return killShotTrack;
    }

    /**
     * Tells you how many kills a player has scored
     * @param playerColor The player identifier
     * @return the number of kills scored by the specified player
     */
    public int countKills(PlayerToken playerColor){
        int number = 0;

        for(List<PlayerToken> kill : killShotTrack){
            for (PlayerToken token : kill){
                if(token == playerColor)
                    number++;
            }
        }

        return number;
    }

    /**
     * @return The list of PlayerTokens that got a kill this game, in descending order. If two players have the same
     *         number of kills, then the faster kill decides the order.
     */
    public List<PlayerToken> getKillShotTrackOrder(){

        List<PlayerToken> scoreHistory = new ArrayList<>();

        // Must build a history of kills to resolve draws
        for(List<PlayerToken> kill : killShotTrack){
            scoreHistory.addAll(kill);
        }
        return Util.uniqueStableSortByCount(scoreHistory);
    }

    /**
     * Return the deck of weapon.
     * @return The deck of weapon.
     */
    public Deck<Weapon> getWeaponDeck(){
        return weaponDeck;
    }

    /**
     * Return the deck of power-up.
     * @return The deck of power-up.
     */
    public Deck<PowerUp> getPowerUpDeck(){
        return powerUpDeck;
    }

    /**
     * Return the deck of ammo tile.
     * @return The deck of ammo tile.
     */
    public Deck<AmmoTile> getAmmoTileDeck(){
        return ammoTileDeck;
    }

    /**
     * Return the Board.
     * @return The Board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Return the number of initial skulls.
     * @return The number of initial skulls.
     */
    public int getInitialSkullNumber() {
        return initialSkullNumber;
    }
}
