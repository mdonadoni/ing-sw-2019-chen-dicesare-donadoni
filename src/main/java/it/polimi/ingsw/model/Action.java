package it.polimi.ingsw.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.util.Json;
import it.polimi.ingsw.util.ResourceException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Action {
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

    public Action(){
        finalFrenzyRequired = false;
        boardNotFlippedRequired = false;
        damageOverwrite = 99;
        damageRequired = 0;
        beforeFirstPlayer = false;
    }

    public static List<Action> loadActions() {
        List<Action> actions = new ArrayList<>();
        try{
            InputStream stream = Action.class.getResourceAsStream("/rules/actions.json");
            ObjectMapper mapper = Json.getMapper();
            JsonNode json = mapper.readTree(stream);

            for(JsonNode node : json){
                Action action = new Action();
                for(JsonNode basic : node.get("basics")){
                    action.addAction(BasicAction.valueOf(basic.asText().toUpperCase()));
                }
                action.setDamageOverwrite(node.get("overwrite").asInt());
                action.setDamageRequired(node.get("required").asInt());
                action.setFinalFrenzyRequired(node.get("finalfrenzyrequired").asBoolean());
                action.setBoardNotFlippedRequired(node.get("boardnotflippedrequired").asBoolean());
                action.setBeforeFirstPlayer(node.get("beforefirstplayer").asBoolean());

                actions.add(action);
            }
        } catch (IOException e){
            throw new ResourceException("Cannot read actions file", e);
        }

        return actions;
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

    public void setBoardNotFlippedRequired(Boolean val){
        boardNotFlippedRequired = val;
    }

    public boolean getBoardNotFlippedRequired(){
        return boardNotFlippedRequired;
    }

    public void setBeforeFirstPlayer(Boolean val){
        beforeFirstPlayer = val;
    }

    public boolean getBeforeFirstPlayer(){
        return  beforeFirstPlayer;
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

        if(player.getDamageTaken().size() < damageRequired || player.getDamageTaken().size() > damageOverwrite)
            res = false;

        return res;
    }
}
