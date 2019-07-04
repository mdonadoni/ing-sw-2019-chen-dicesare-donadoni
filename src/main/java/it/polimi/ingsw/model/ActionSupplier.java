package it.polimi.ingsw.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.util.Json;
import it.polimi.ingsw.util.ResourceException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represent an instance of action supplier with a list of actions.
 */
public class ActionSupplier {
    /**
     * The instance of the class.
     */
    private static ActionSupplier actionSupplierInstance;
    /**
     * The list of actions.
     */
    private List<Action> actions;

    /**
     * Constructor of the class, it load the action in the list.
     */
    private ActionSupplier(){
        actions = new ArrayList<>();
        loadActions();
    }

    /**
     * Return the instance of the class, if it doesn't exist then instantiate it anc return it.
     * @return The instance of the class.
     */
    public static ActionSupplier getInstance(){
        if(actionSupplierInstance == null){
            actionSupplierInstance = new ActionSupplier();
        }

        return actionSupplierInstance;
    }

    /**
     * This method load the type of action from the file.
     */
    private void loadActions() {
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

                JsonNode always = node.get("alwaysavailable");
                if(always != null)
                    action.setAlwaysavailable(always.asBoolean());

                JsonNode endsTurn = node.get("endsTurn");
                if(endsTurn != null)
                    action.setEndsTurn(endsTurn.asBoolean());

                actions.add(action);
            }
        } catch (IOException e){
            throw new ResourceException("Cannot read actions file", e);
        }
    }

    /**
     * Return the list of actions.
     * @return The list of actions to return.
     */
    public List<Action> getActions(){
        return new ArrayList<>(actions);
    }

}
