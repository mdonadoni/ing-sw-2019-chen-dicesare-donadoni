package it.polimi.ingsw.network.socket.messages.server;

import it.polimi.ingsw.network.socket.ViewSideHandler;
import it.polimi.ingsw.network.socket.messages.ServerToView;

/**
 * Abstract class that represents a generic response of a server method.
 */
public abstract class ResponseServerMethod extends ServerToView {
    /**
     * Constructor of ResponseServerMethod.
     * @param uuid uuid of the corresponding request.
     */
    ResponseServerMethod(String uuid) {
        super(uuid);
    }

    /**
     * Method used to handle the response to a server method (Visitor Pattern).
     * @param handler Handler of a server method response.
     */
    @Override
    public void visit(ViewSideHandler handler) {
        handler.handle(this);
    }
}
