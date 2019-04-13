package it.polimi.ingsw.model;

import it.polimi.ingsw.model.weapons.HistorySorter;
import it.polimi.ingsw.model.weapons.Weapon;
import java.util.*;
import static java.util.stream.Collectors.*;

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
     * Standard constructor
     */
    public GameBoard(){
       this(8); // Shall we do a config file with all the constants?
    }

    /**
     * Constructor that allows to specify the number of skulls
     * @param skulls number of skulls to be placed on the gameboard
     */
    public GameBoard(int skulls){
        remainingSkulls = skulls;
        initialSkullNumber = skulls;
        killShotTrack = new ArrayList<>();
        weaponDeck = new Deck<>();
        powerUpDeck = new Deck<>();
        ammoTileDeck = new Deck<>();
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
        EnumMap<PlayerToken, Integer> playerScores = new EnumMap<>(PlayerToken.class);
        HistorySorter hSorter;

        // Must build a history of kills to resolve draws
        for(List<PlayerToken> kill : killShotTrack){
            scoreHistory.addAll(kill);
        }
        hSorter = new HistorySorter(scoreHistory);
        for (PlayerToken player : PlayerToken.values())
            playerScores.put(player, countKills(player));

        // Sorts the EnumMap
        return playerScores
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder((p1, p2) -> hSorter.compare(p1.getKey(), p2.getKey())))
                .map(Map.Entry::getKey)
                .collect(toList());
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
}