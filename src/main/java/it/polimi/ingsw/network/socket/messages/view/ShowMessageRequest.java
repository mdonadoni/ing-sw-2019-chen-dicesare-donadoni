package it.polimi.ingsw.network.socket.messages.view;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.network.LocalView;

/**
 * Request the view to show a message.
 */
public class ShowMessageRequest extends RequestViewMethod {
    /**
     * Message to be shown
     */
    private String message;

    /**
     * Constructor of ShowMessageRequest.
     * @param message Message to be shown.
     */
    @JsonCreator
    public ShowMessageRequest(@JsonProperty("message") String message) {
        this.message = message;
    }

    /**
     * Command Pattern. Show message on the given view.
     * @param view Method is invoked on this view
     * @return null (this request has no response).
     */
    @Override
    public ResponseViewMethod invokeOn(LocalView view) {
        view.showMessage(message);
        return new VoidResponse(this);
    }

    @Override
    public String toString() {
        return "[" + getUUID() + "] ShowMessageRequest (" + message + ")";
    }
}
