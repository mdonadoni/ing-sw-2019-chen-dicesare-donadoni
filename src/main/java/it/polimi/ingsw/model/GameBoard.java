package it.polimi.ingsw.model;

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
}
