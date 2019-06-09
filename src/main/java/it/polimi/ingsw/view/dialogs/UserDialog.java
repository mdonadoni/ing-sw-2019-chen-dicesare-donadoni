package it.polimi.ingsw.view.dialogs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.util.Json;
import it.polimi.ingsw.util.ResourceException;
import it.polimi.ingsw.util.ResourceManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDialog {

    public static Map<DialogType, String> loadDialogs(){
        try{
            Map<DialogType, String> dialogMap = new HashMap<>();
            InputStream stream = ResourceManager.get("/dialogs/dialoglines.json");
            ObjectMapper mapper = Json.getMapper();
            JsonNode json = mapper.readTree(stream);

            // Insert the dialogs in the hashmap
            for(JsonNode node : json){
                dialogMap.put(DialogType.valueOf(node.get("name").asText().toUpperCase()), node.get("payload").asText());
            }

            return dialogMap;
        } catch (IOException e){
            throw new ResourceException("Cannot read dialog lines file", e);
        }
    }

    public static String getDialog(DialogType type){
        return getDialog(type, new ArrayList<String>());
    }

    public static String getDialog(DialogType type, List<String> params){
        Map<DialogType, String> dialogMap = loadDialogs();
        String dialogString = dialogMap.get(type);

        for(String word : params)
            dialogString = dialogString.replaceFirst("@@@", word);

        return dialogString;
    }
}
