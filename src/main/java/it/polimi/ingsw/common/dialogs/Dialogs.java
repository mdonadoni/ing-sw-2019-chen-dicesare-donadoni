package it.polimi.ingsw.common.dialogs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.util.Json;
import it.polimi.ingsw.util.ResourceException;
import it.polimi.ingsw.util.ResourceManager;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class Dialogs {
    private static Map<Dialog, String> dialogMap = null;

    private Dialogs() {}

    private static void loadDialogs(){
        try{
            dialogMap = new HashMap<>();
            InputStream stream = ResourceManager.get("/dialogs/dialogs.json");
            ObjectMapper mapper = Json.getMapper();
            JsonNode json = mapper.readTree(stream);

            // Insert the dialogs in the hashmap
            for(Dialog dialog : Dialog.values()){
                String key = dialog.name().toLowerCase();
                if (!json.has(key)) {
                    throw new ResourceException("Cannot find dialog " + key);
                }
                String value = json.get(key).asText();
                if (value.length() == 0) {
                    throw new ResourceException("Cannot find value of " + key);
                }
                dialogMap.put(dialog, value);
            }
        } catch (IOException e){
            throw new ResourceException("Cannot read dialog lines file", e);
        }
    }

    public static synchronized String getDialog(Dialog type, String ...params){
        if (dialogMap == null) {
            loadDialogs();
        }

        String dialogString = dialogMap.get(type);
        return MessageFormat.format(dialogString, (Object[]) params);
    }
}
