package it.polimi.ingsw.network.socket.messages.view;

import it.polimi.ingsw.network.socket.ServerSideHandler;
import it.polimi.ingsw.network.socket.messages.ViewToServer;

/**
 * Abstract class that represents a generic response of a view method.
 */
public abstract class ResponseViewMethod extends ViewToServer {
    /**
     * Constructor of ResponseViewMethod.
     * @param uuid uuid of the corresponding request.
     */
    ResponseViewMethod(String uuid) {
        super(uuid);
    }

    /**
     * Method used to handle the response to a view method (Visitor Pattern).
     * @param handler Handler of a view method response.
     */
    @Override
    public void visit(ServerSideHandler handler) {
        handler.handle(this);
    }
}
