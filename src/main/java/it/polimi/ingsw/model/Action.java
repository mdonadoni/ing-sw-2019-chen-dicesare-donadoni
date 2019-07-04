package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent an action that a player can perform.
 */
public class Action extends Identifiable{
    /**
     * The list of basic action that compose the action.
     * @see BasicAction
     */
    private List<BasicAction> actions = new ArrayList<>();

    /**
     * Some Actions can be done in Final Frenzy, others can be done while Final Frenzy isn't active.
     */
    private boolean finalFrenzyRequired;

    /**
     * Some Actions cannot be performed if the board has been flipped.
     * If boardNotFlippedRequired is true, the action cannot be done if the board has been flipped.
     * If boardNotFlippedRequired is false, the action can be done in any case.
     */
    private boolean boardNotFlippedRequired;

    /**
     * Some actions require a certain amount of damage taken in order to be performed.
     */
    private int damageRequired;

    /**
     * Some actions can be overwritten by other, better, actions. This happens when the player reaches this
     * amount of damage taken.
     */
    private int damageOverwrite;

    /**
     * Some frenzy actions can be done only if you are before/after the first player.
     */
    private boolean beforeFirstPlayer;
    /**
     * Some actions are always available during a player turn e.g. skip the turn.
     */
    private boolean alwaysavailable;
    /**
     * Some actions end the turn after their execution.
     */
    private boolean endsTurn;

    /**
     * Constructor of the class.
     */
    public Action(){
        finalFrenzyRequired = false;
        boardNotFlippedRequired = false;
        damageOverwrite = 99;
        damageRequired = 0;
        beforeFirstPlayer = false;
        alwaysavailable = false;
        endsTurn = false;
    }

    /**
     * Add a basic action.
     * @param act the basic action to add.
     */
    public void addAction(BasicAction act){
        actions.add(act);
    }

    /**
     * Remove a basic action.
     * @param act The basic action to remove.
     */
    public void removeAction(BasicAction act){
        actions.remove(act);
    }

    /**
     * Count the number of movement in the action.
     * @return The number of movement in the action.
     */
    public int countMovement(){
        return (int)actions
                .stream()
                .filter(e -> e.equals(BasicAction.MOVEMENT))
                .count();
    }

    /**
     * Return if the action contains a movement.
     * @return True if the action contains a movement, false otherwise.
     */
    public boolean canMove(){
        return actions.contains(BasicAction.MOVEMENT);
    }
    /**
     * Return if the action contains a grab action.
     * @return True if the action contains a grab action, false otherwise.
     */
    public boolean canGrab(){
        return actions.contains(BasicAction.GRAB);
    }
    /**
     * Return if the action contains a shoot action.
     * @return True if the action contains a shoot action, false otherwise.
     */
    public boolean canShoot(){
        return actions.contains(BasicAction.SHOOT);
    }
    /**
     * Return if the action contains a reload action.
     * @return True if the action contains a reload action, false otherwise.
     */
    public boolean canReload(){
        return actions.contains(BasicAction.RELOAD);
    }

    /**
     * Set if the action need the final frenzy to be executed.
     * @param val The boolean value that set the field, true if it need the final frenzy, false otherwise.
     */
    public void setFinalFrenzyRequired(Boolean val){
        finalFrenzyRequired = val;
    }
    /**
     * Get if the action need the final frenzy to be executed.
     * @return True if final frenzy is required, false otherwise
     */
    public boolean getFinalFrenzyRequired(){
        return finalFrenzyRequired;
    }

    /**
     * Set the damage required to perform the action.
     * @param val The amount of damage required.
     */
    public void setDamageRequired(int val){
        damageRequired = val;
    }

    /**
     * Get the damage required to perform the action.
     * @return The amount of damage required.
     */
    public int getDamageRequired(){
        return damageRequired;
    }

    /**
     * Set the damage to overwrite the action with another one.
     * @param val The amount of damage that overwrite the action.
     */
    public void setDamageOverwrite(int val){
        damageOverwrite = val;
    }

    /**
     * Get the damage to overwrite the action with another one.
     * @return The amount of damage that overwrite the action.
     */
    public int getDamageOverwrite(){
        return damageOverwrite;
    }

    /**
     * Set if the action needs the board to be not flipped in order to be performed.
     * @param val The boolean value that set the field, true if it need the board to be not flipped, false otherwise.
     */
    public void setBoardNotFlippedRequired(boolean val){
        boardNotFlippedRequired = val;
    }

    /**
     * Get if the action needs the board to be not flipped in order to be performed.
     * @return The boolean value that set the field, true if it need the board to be not flipped, false otherwise.
     */
    public boolean getBoardNotFlippedRequired(){
        return boardNotFlippedRequired;
    }

    public void setBeforeFirstPlayer(boolean val){
        beforeFirstPlayer = val;
    }

    public boolean getBeforeFirstPlayer(){
        return  beforeFirstPlayer;
    }

    public void setAlwaysavailable(boolean val){
        alwaysavailable = val;
    }

    public boolean getAlwaysavailable(){
        return alwaysavailable;
    }

    public List<BasicAction> getActions(){
        return actions;
    }

    /**
     * This method tells whether given player can perform this action
     * @param player the player to analyze
     * @param finalFrenzyActive if the final frenzy is active
     * @return True if the Action can be done, False otherwise
     */
    public boolean canPerform(Player player, Boolean finalFrenzyActive){
        boolean res = true;

        if(boardNotFlippedRequired && player.isBoardFlipped())
            res = false;

        if(finalFrenzyRequired != finalFrenzyActive)
            res = false;

        if(finalFrenzyRequired && (beforeFirstPlayer != player.getBeforeFirstPlayerFF())){
            res = false;
        }

        if(player.getDamageTaken().size() < damageRequired || player.getDamageTaken().size() >= damageOverwrite)
            res = false;

        if(alwaysavailable)
            res = true;

        return res;
    }

    /**
     * Return if the action is free.
     * @return True the action is not free, flase otherwise.
     */
    public boolean expendsUse(){
        return !(actions.size() == 1 && actions.get(0).equals(BasicAction.POWERUP));
    }

    /**
     * Get if the action ends the turn.
     * @return True if the action ends the turn, false otherwise.
     */
    public boolean getEndsTurn(){
        return endsTurn;
    }

    /**
     * Set if the action ends the turn.
     * @param ends True if the action ends the turn, false otherwise.
     */
    public void setEndsTurn(boolean ends){
        endsTurn = ends;
    }

    /**
     * Return if the action is only made of movement.
     * @return True if the action is only movement, false otherwise.
     */
    public boolean canOnlyMove(){
        boolean res = true;

        for(BasicAction basic : actions){
            if (basic != BasicAction.MOVEMENT)
                res = false;
        }

        return res;
    }

    /**
     * Return the information of the action.
     * @return String of basic action that compose the action.
     */
    public String info(){
        String retString = "";

        for(BasicAction basic : actions)
            retString = retString.concat(basic.toString() + " ");

        return retString;
    }
}
