package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Action extends Identifiable{
    private List<BasicAction> actions = new ArrayList<>();

    /**
     * Some Actions can be done in Final Frenzy, others can be done while Final Frenzy isn't active
     */
    private boolean finalFrenzyRequired;

    /**
     * Some Actions cannot be performed if the board has been flipped.
     * If boardNotFlippedRequired is true, the action cannot be done if the board has been flipped.
     * If boardNotFlippedRequired is false, the action can be done in any case.
     */
    private boolean boardNotFlippedRequired;

    /**
     * Some actions require a certain amount of damage taken in order to be performed
     */
    private int damageRequired;

    /**
     * Some actions can be overwritten by other, better, actions. This happens when the player reaches this
     * amount of damage taken
     */
    private int damageOverwrite;

    /**
     * Some frenzy actions can be done only if you are before/after the first player
     */
    private boolean beforeFirstPlayer;

    private boolean alwaysavailable;

    public Action(){
        finalFrenzyRequired = false;
        boardNotFlippedRequired = false;
        damageOverwrite = 99;
        damageRequired = 0;
        beforeFirstPlayer = false;
        alwaysavailable = false;
    }

    public void addAction(BasicAction act){
        actions.add(act);
    }

    public void removeAction(BasicAction act){
        actions.remove(act);
    }

    public int countMovement(){
        return (int)actions
                .stream()
                .filter(e -> e.equals(BasicAction.MOVEMENT))
                .count();
    }

    public boolean canMove(){
        return actions.contains(BasicAction.MOVEMENT);
    }

    public boolean canGrab(){
        return actions.contains(BasicAction.GRAB);
    }

    public boolean canShoot(){
        return actions.contains(BasicAction.SHOOT);
    }

    public boolean canReload(){
        return actions.contains(BasicAction.RELOAD);
    }

    public void setFinalFrenzyRequired(Boolean val){
        finalFrenzyRequired = val;
    }

    public boolean getFinalFrenzyRequired(){
        return finalFrenzyRequired;
    }

    public void setDamageRequired(int val){
        damageRequired = val;
    }

    public int getDamageRequired(){
        return damageRequired;
    }

    public void setDamageOverwrite(int val){
        damageOverwrite = val;
    }

    public int getDamageOverwrite(){
        return damageOverwrite;
    }

    public void setBoardNotFlippedRequired(boolean val){
        boardNotFlippedRequired = val;
    }

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

    public boolean expendsUse(){
        return !(actions.size() == 1 && actions.get(0).equals(BasicAction.POWERUP));
    }

    public boolean endsTurn(){
        return (actions.size() == 1 && actions.get(0).equals(BasicAction.RELOAD));
    }

    public boolean canOnlyMove(){
        boolean res = true;

        for(BasicAction basic : actions){
            if (basic != BasicAction.MOVEMENT)
                res = false;
        }

        return res;
    }

    public String info(){
        String retString = new String();

        for(BasicAction basic : actions)
            retString = retString.concat(basic.toString() + " ");

        return retString;
    }
}
