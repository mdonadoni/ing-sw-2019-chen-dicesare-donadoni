package it.polimi.ingsw.network.socket.messages;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.network.socket.ServerSideHandler;

/**
 * Generic message sent from the view to the server.
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, property="@type")
public abstract class ViewToServer extends Message{
    /**
     * Visitor patter. Let the given ServerSideHandler handle this message.
     * @param handler Handler that handles this message.
     */
    public abstract void visit(ServerSideHandler handler);

    /**
     * Constructor for ViewToServer with random uuid.
     */
    protected ViewToServer() {
        super();
    }

    /**
     * Constructor for ViewToServer with given uuid.
     * @param uuid uuid of the message.
     */
    protected ViewToServer(String uuid) {
        super(uuid);
    }
}
