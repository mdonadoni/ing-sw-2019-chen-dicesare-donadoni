package it.polimi.ingsw.model;

public class Turn {
    private TurnType type;
    private Player currentPlayer;
    private int movesMade;

    public Turn(){
        movesMade = 0;
    }

    public Turn(TurnType type, Player currentPlayer){
        this.type = type;
        this.currentPlayer = currentPlayer;
    }

    public TurnType getType() {
        return type;
    }

    public void setType(TurnType type) {
        this.type = type;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void resetMovesMade() {
        movesMade = 0;
    }

    public void addMove() {
        movesMade++;
    }

    public int getMovesMade(){
        return movesMade;
    }
}
