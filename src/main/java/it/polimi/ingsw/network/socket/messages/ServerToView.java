package it.polimi.ingsw.network.socket.messages;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.network.socket.ViewSideHandler;

/**
 * Generic message sent from the server to the view.
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, property="@type")
public abstract class ServerToView extends Message {
    /**
     * Visitor patter. Let the given ViewSideHandler handle this message.
     * @param handler Handler that handles this message.
     */
    public abstract void visit(ViewSideHandler handler);

    /**
     * Constructor of ServerToView with random uuid.
     */
    protected ServerToView() {
        super();
    }

    /**
     * Constructor of ServerToView with given uuid.
     * @param uuid uuid of the message.
     */
    protected ServerToView(String uuid) {
        super(uuid);
    }
}
