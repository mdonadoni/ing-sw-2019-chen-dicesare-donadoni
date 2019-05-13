package it.polimi.ingsw.model;

public class Turn {
    private TurnType type;
    private Player currentPlayer;
    private int movesMade;

    public Turn(Player currentPlayer, TurnType type){
        this.type = type;
        this.currentPlayer = currentPlayer;
        this.movesMade = 0;
    }

    public TurnType getType() {
        return type;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void addMove() {
        movesMade++;
    }

    public int getMovesMade(){
        return movesMade;
    }
}
