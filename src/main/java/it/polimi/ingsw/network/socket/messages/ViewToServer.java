package it.polimi.ingsw.network.socket.messages;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.network.socket.ServerSideHandler;

/**
 * Generic message sent from the view to the server.
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, property="@type")
public interface ViewToServer {
    /**
     * Visitor patter. Let the given ServerSideHandler handle this message.
     * @param handler Handler that handles this message.
     */
    void visit(ServerSideHandler handler);
}
