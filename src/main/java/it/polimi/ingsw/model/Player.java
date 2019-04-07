package it.polimi.ingsw.model;

/**
 * Mock class representing a Player
 */
public class Player {
    private Square square;
    private String name;
    public Player(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public Square getSquare(){return square;}
    public void setSquare(Square square){
        this.square = square;
    }
}
