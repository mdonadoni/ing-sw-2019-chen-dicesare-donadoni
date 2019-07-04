package it.polimi.ingsw.common.dialogs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.util.Json;
import it.polimi.ingsw.util.ResourceException;
import it.polimi.ingsw.util.ResourceManager;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.EnumMap;
import java.util.Map;

/**
 * This class maps a type of dialog to a string of message used to communicate with the user.
 */
public class Dialogs {
    /**
     * The map that associate a type of dialog with its string.
     */
    private static Map<Dialog, String> dialogMap = null;

    /**
     * Constructor of the class.
     */
    private Dialogs() {}

    /**
     * This method loads from a file the strings to associate with the type of dialog.
     */
    private static void loadDialogs(){
        try{
            dialogMap = new EnumMap<>(Dialog.class);
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

    /**
     * This method return the message of the dialog.
     * @param type The type of dialog to get.
     * @param params Array of string to fill some messages when is necessary, e.g. when the message use a players
     *               nickname or when the input must be in a certain range of values.
     * @return The string of the message required.
     */
    public static synchronized String getDialog(Dialog type, String ...params){
        if (dialogMap == null) {
            loadDialogs();
        }

        String dialogString = dialogMap.get(type);
        return MessageFormat.format(dialogString, (Object[]) params);
    }
}
