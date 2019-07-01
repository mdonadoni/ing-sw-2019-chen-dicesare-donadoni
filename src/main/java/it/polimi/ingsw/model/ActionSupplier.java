package it.polimi.ingsw.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.util.Json;
import it.polimi.ingsw.util.ResourceException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ActionSupplier {

    private static ActionSupplier INSTANCE;
    private List<Action> actions;

    private ActionSupplier(){
        actions = new ArrayList<>();
        loadActions();
    }

    public static ActionSupplier getInstance(){
        if(INSTANCE == null){
            INSTANCE = new ActionSupplier();
        }

        return INSTANCE;
    }

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

    public List<Action> getActions(){
        return new ArrayList<>(actions);
    }

}
