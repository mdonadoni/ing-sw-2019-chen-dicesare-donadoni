package it.polimi.ingsw.model;

/**
 * This class represent turn during a match
 */
public class Turn {
    /**
     * Type of turn.
     */
    private TurnType type;
    /**
     * The current player of the turn.
     */
    private Player currentPlayer;
    /**
     * The number of moves made in this turn.
     */
    private int movesMade;

    /**
     * Constructor of the class
     * @param currentPlayer The current player.
     * @param type The type of turn.
     */
    public Turn(Player currentPlayer, TurnType type){
        this.type = type;
        this.currentPlayer = currentPlayer;
        this.movesMade = 0;
    }

    /**
     * Get the type of the turn.
     * @return The type of the turn.
     */
    public TurnType getType() {
        return type;
    }

    /**
     * Get the current player.
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Add a move made.
     */
    public void addMove() {
        movesMade++;
    }

    /**
     * Get the number of moves made.
     * @return The number of moves made.
     */
    public int getMovesMade(){
        return movesMade;
    }
}
