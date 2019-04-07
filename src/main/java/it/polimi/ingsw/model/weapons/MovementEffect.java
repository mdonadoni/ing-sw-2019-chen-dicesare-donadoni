package it.polimi.ingsw.model.weapons;

public class MovementEffect extends Effect {
    private boolean visibleDest;
    private boolean line;
    private boolean self;
    private boolean fixed;

    /**
     * @param value number to be assigned to amount
     */
    public MovementEffect(int value){
        setAmount(value);
    }

    /**
     * @return Whether the destination of the movement must be visible to the player
     */
    public boolean isVisibleDest() {
        return visibleDest;
    }

    /**
     * @param visibleDest Boolean that specifies whether the destination must be visible to the
     *                    player
     */
    public void setVisibleDest(boolean visibleDest) {
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