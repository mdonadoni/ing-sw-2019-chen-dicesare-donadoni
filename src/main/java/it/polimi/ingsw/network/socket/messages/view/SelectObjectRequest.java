package it.polimi.ingsw.network.socket.messages.view;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.network.LocalView;
import it.polimi.ingsw.network.socket.messages.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Request to select squares to the view
 */
public class SelectObjectRequest extends RequestViewMethod {
    /**
     * List of squares' coordinates.
     */
    private ArrayList<String> objUuid;

    /**
     * Minimum number of squares to be chosen.
     */
    private int min;

    /**
     * Maximum number of squares to be chosen.
     */
    private int max;


    /**
     * Enum that represents the dialog type
     */
    private Dialog dialog;

    /**
     * Constructor of a SelectObjectRequest.
     * @param objUuid List of squares' coordinates.
     * @param min Minimum number of squares to be chosen.
     * @param max Maximum number of squares to be chosen.
     * @param dialog the dialog to be shown
     */
    @JsonCreator
    public SelectObjectRequest(@JsonProperty("objUuid") List<String> objUuid,
                               @JsonProperty("min") int min,
                               @JsonProperty("max") int max,
                               @JsonProperty("dialog") Dialog dialog) {
        this.objUuid = new ArrayList<>(objUuid);
        this.min = min;
        this.max = max;
        this.dialog = dialog;
    }

    /**
     * Command Pattern.
     * @param view Method is invoked on this view
     * @return Response to method invocation.
     */
    @Override
    public Message invokeOn(LocalView view) {
        return new SelectObjectResponse(this, view.selectObject(objUuid, min, max, dialog));
    }
}
