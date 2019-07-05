package it.polimi.ingsw.model.weapons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represent a bonus movement effect.
 */
public class MovementEffect extends Effect {
    /**
     * Visibility of the destination.
     */
    private Visibility visibleDest;
    /**
     * Define if the movement must be done in a line.
     */
    private boolean line;
    /**
     * Define if the movement is referred to the current player.
     */
    private boolean self;
    /**
     * Define if the movement is forced by another action.
     */
    private boolean fixed;

    /**
     * Constructor
     * @param value number to be assigned to amount
     */
    @JsonCreator
    public MovementEffect(@JsonProperty("amount") int value){
        setValue(value);
    }

    /**
     * Standard constructor
     */
    public MovementEffect(){
    }

    /**
     * Return if the destination of the movement must be visible to the player.
     * @return Whether the destination of the movement must be visible to the player.
     */
    public Visibility isVisibleDest() {
        return visibleDest;
    }

    /**
     * Set if the destination must be visible to the player.
     * @param visibleDest Boolean that specifies whether the destination must be visible to the player.
     */
    public void setVisibleDest(Visibility visibleDest) {
        this.visibleDest = visibleDest;
    }

    /**
     * Return if the movement must be done in a line.
     * @return Whether the movement must be done in a line.
     */
    public boolean isLine() {
        return line;
    }

    /**
     * Set if the movement must be done in a line.
     * @param line Boolean that defines whether the movement must be done in a line.
     */
    public void setLine(boolean line) {
        this.line = line;
    }

    /**
     * Return if the movement is referred to the current player.
     * @return True if the movement is referred to the current player, false otherwise.
     */
    public boolean isSelf() {
        return self;
    }

    /**
     * Set if the movement is referred to the current player.
     * @param self True if the movement is referred to the current player. False otherwise.
     */
    public void setSelf(boolean self) {
        this.self = self;
    }

    /**
     * Return if the movement is forced by another action.
     * @return True if the movement is forced by another action.
     */
    public boolean isFixed() {
        return fixed;
    }

    /**
     * Set if the movement is forced by another action.
     * @param fixed True if the movement is forced by another action.
     */
    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }
}