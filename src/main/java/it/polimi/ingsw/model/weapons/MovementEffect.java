package it.polimi.ingsw.model.weapons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class MovementEffect extends Effect {
    private Visibility visibleDest;
    private boolean line;
    private boolean self;
    private boolean fixed;

    /**
     * @param value number to be assigned to amount
     */
    @JsonCreator
    public MovementEffect(@JsonProperty("amount") int value){
        setValue(value);
    }

    MovementEffect(JsonNode json){
        setValue(json.get("value").asInt());
        setVisibleDest(Visibility.valueOf(json.get("visibleDest").asText().toUpperCase()));
        setLine(json.get("line").asBoolean());
        setFixed(json.get("fixed").asBoolean());
        setSelf(json.get("self").asBoolean());
    }

    /**
     * @return Whether the destination of the movement must be visible to the player
     */
    public Visibility isVisibleDest() {
        return visibleDest;
    }

    /**
     * @param visibleDest Boolean that specifies whether the destination must be visible to the
     *                    player
     */
    public void setVisibleDest(Visibility visibleDest) {
        this.visibleDest = visibleDest;
    }

    /**
     * @return Whether the movement must be done in a line
     */
    public boolean isLine() {
        return line;
    }

    /**
     * @param line Boolean that defines whether the movement must be done in a line
     */
    public void setLine(boolean line) {
        this.line = line;
    }

    /**
     * @return True if the movement is referred to the current player. False otherwise
     */
    public boolean isSelf() {
        return self;
    }

    /**
     * @param self True if the movement is referred to the current player. False otherwise
     */
    public void setSelf(boolean self) {
        this.self = self;
    }

    /**
     * @return True if the movement is forced by another action
     */
    public boolean isFixed() {
        return fixed;
    }

    /**
     * @param fixed True if the movement is forced by another action
     */
    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }
}