package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Match {
    private static int matchCreated = 0;
    /**
     * Unique match identifier
     */
    private int id;
    /**
     * States whether the match in in final frenzy
     */
    private boolean finalFrenzy;
    /**
     * List containing all the players participating in this match
     */
    private List<Player> players = new ArrayList<>();
    /**
     * The Gameboard associated with this match
     */
    private GameBoard gameBoard = new GameBoard();
    /**
     * Contains all the information needed for the current turn
     */
    private Turn currentTurn;
    /**
     * States whether the game has started
     */
    private MatchStatus status;
    /**
     * Counts the number of turns
     */
    private int turnsElapsed;

    /**
     * Standard constructor, generates his id
     */
    public Match(){
        id = matchCreated;
        matchCreated++;
        finalFrenzy = false;
    }

    /**
     * Gives the match unique identifier
     * @return the match id
     */
    public int getId(){
        return id;
    }

    /**
     * Adds a player to the match
     * @param player the player to be added
     */
    public void addPlayer(Player player){
        if(players.size() < 5)
            players.add(player);
        else
            throw new InvalidOperationException("Maximum number of active players reached");
    }

    /**
     * Gives you the list of players currently participating in this match
     * @return The list of players currently participating in this match
     */
    public List<Player> getPlayers(){
        return new ArrayList<>(players);
    }

    /**
     * Sets final frenzy mode to true
     */
    public void activateFinalFrenzy(){
        finalFrenzy = true;
    }

    /**
     * Starts a new game by initiating what needs to be instantiated
     */
    public void startMatch(){
        currentTurn = new Turn(TurnType.FIRST_TURN, players.get(0));
        turnsElapsed = 1;
    }

    /**
     * Gives you the gameboard associated with this match
     * @return the gameboard fo the match
     */
    public GameBoard getGameBoard(){
        return gameBoard;
    }

    /**
     * Gives you all the information about this particular turn
     * @return The current turn
     */
    public Turn getCurrentTurn(){
        return currentTurn;
    }

    /**
     * Goes to the next turn
     */
    public void nextTurn(){
        turnsElapsed++;
        if(players.indexOf(currentTurn.getCurrentPlayer()) < players.size()-1)
            currentTurn.setCurrentPlayer(players.get(players.indexOf(currentTurn.getCurrentPlayer())+1));
        else
            currentTurn.setCurrentPlayer(players.get(0));

        currentTurn.resetMovesMade();

        if (turnsElapsed <= players.size())
            currentTurn.setType(TurnType.FIRST_TURN);
        else if(finalFrenzy)
            currentTurn.setType(TurnType.FINAL_FRENZY);
        else
            currentTurn.setType(TurnType.STANDARD);
    }

    /**
     * Sets the status of the match
     * @param status the status to be set
     */
    public void setStatus(MatchStatus status){
        this.status = status;
    }

    /**
     * Gives you the status of the match
     * @return the status of the match
     */
    public MatchStatus getStatus(){
        return status;
    }

    /**
     * This method returns true if there is a sufficient number of active players to play the match. Returns false
     * otherwise.
     * @return Whether the match is active
     */
    public boolean isActive(){
        int activePlayers = 0;

        for(Player player : players)
            if(player.getActive())
                activePlayers++;

        return activePlayers >= 3;
    }
}
