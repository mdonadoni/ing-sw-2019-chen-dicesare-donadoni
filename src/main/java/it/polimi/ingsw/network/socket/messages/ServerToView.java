package it.polimi.ingsw.network.socket.messages;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.network.socket.ViewSideHandler;

/**
 * Generic message sent from the server to the view.
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, property="@type")
public interface ServerToView {
    /**
     * Visitor patter. Let the given ViewSideHandler handle this message.
     * @param handler Handler that handles this message.
     */
    void visit(ViewSideHandler handler);
}
