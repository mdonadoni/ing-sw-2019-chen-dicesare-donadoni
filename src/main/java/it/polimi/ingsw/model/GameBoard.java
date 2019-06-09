package it.polimi.ingsw.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.util.Json;
import it.polimi.ingsw.util.ResourceException;
import it.polimi.ingsw.util.ResourceManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Class representing the GameBoard
 */
public class GameBoard {
    /**
     * Number of initial skulls for this gameboard
     */
    private int initialSkullNumber;
    /**
     * Number of skulls left on the gameboard
     */
    private int remainingSkulls;
    /**
     * Keeps track of the kills scored by the players, every kill might be a multiple one, so we need a list of lists
     */
    private List<List<PlayerToken>> killShotTrack;
    /**
     * A deck containing the weapon cards
     */
    Deck<Weapon> weaponDeck;
    /**
     * A deck containing the powerup cards
     */
    Deck<PowerUp> powerUpDeck;
    /**
     * A deck containing thr ammo tiles
     */
    Deck<AmmoTile> ammoTileDeck;

    /**
     * Board with square and spawnpoints.
     */
    Board board;

    /**
     * Standard constructor
     */
    GameBoard() throws ResourceException {
    }

    /**
     * Constructor that allows to specify the number of skulls
     * @param skulls number of skulls to be placed on the gameboard
     */
    public GameBoard(int skulls, BoardType boardType){
        remainingSkulls = skulls;
        initialSkullNumber = skulls;
        killShotTrack = new ArrayList<>();
        weaponDeck = new Deck<>();
        powerUpDeck = new Deck<>();
        ammoTileDeck = new Deck<>();
        board = new Board(boardType);

        initPowerUpDeck();
        initAmmoTileDeck();
    }

    private void initPowerUpDeck(){
        ObjectMapper mapper = Json.getMapper();
        InputStream stream = ResourceManager.get("/decks/powerUpDeck.json");
        try {
            JsonNode json = mapper.readTree(stream);

            for (JsonNode pwu : json){
                for (int i = 0; i<2; i++)
                    powerUpDeck.add(new PowerUp(pwu));
            }

            powerUpDeck.shuffle();
        } catch (IOException e){
            throw new ResourceException("Cannot read powerUpDeck resource", e);
        }
    }

    private void initAmmoTileDeck(){
        ObjectMapper mapper = Json.getMapper();
        InputStream stream = ResourceManager.get("/decks/ammoTileDeck.json");
        try{
            JsonNode json = mapper.readTree(stream);

            for(JsonNode ammoTile : json){
                for(int i = 0; i<3; i++){
                    ammoTileDeck.add(new AmmoTile(ammoTile));
                }
            }
            ammoTileDeck.shuffle();
        } catch(IOException e){
            throw new ResourceException("Cannot read ammoTileDeck resource", e);
        }
    }

    /**
     * Goes through all the StandardSquares and if the ammoTile is missing, adds one
     */
    public void refillAmmoTile() {
        for(StandardSquare sq : board.getStandardSquares()){
            if(!sq.hasAmmoTile()){
                sq.setAmmoTile(ammoTileDeck.draw());
            }
        }
    }

    /**
     * Add a kill to the killShotTrack, this method also decreases the number of skulls left. According to the rules, if
     * there are no skulls left, all the kills are appended to the last kill scored.
     * @param tokens the tokens of the player who scored the kill
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

    public int getRemainingSkulls(){
        return remainingSkulls;
    }

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

    public Deck<Weapon> getWeaponDeck(){
        return weaponDeck;
    }

    public Deck<PowerUp> getPowerUpDeck(){
        return powerUpDeck;
    }

    public Deck<AmmoTile> getAmmoTileDeck(){
        return ammoTileDeck;
    }

    public void initBoard(BoardType bdType) throws ResourceException{
        board = new Board(bdType);
    }

    public Board getBoard() {
        return board;
    }

    public int getInitialSkullNumber() {
        return initialSkullNumber;
    }
}