package it.polimi.ingsw.network.socket.messages.view;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.minified.MiniModel;
import it.polimi.ingsw.network.LocalView;
import it.polimi.ingsw.network.socket.messages.Message;

/**
 * Request a model update.
 */
public class UpdateModelRequest extends RequestViewMethod {

    /**
     * Updated model.
     */
    private MiniModel model;

    /**
     * Constructor of UpdateModelRequest.
     * @param model Updated model to be sent.
     */
    @JsonCreator
    public UpdateModelRequest(@JsonProperty("model") MiniModel model) {
        this.model = model;
    }

    /**
     * Command pattern. Invoke update on given view.
     * @param view Method is invoked on this view.
     * @return Void response
     */
    @Override
    public Message invokeOn(LocalView view) {
        view.updateModel(model);
        return new VoidResponse(this);
    }
}
