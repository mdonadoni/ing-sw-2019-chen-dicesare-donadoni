package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.network.socket.messages.view.DisconnectRequest;
import it.polimi.ingsw.network.socket.messages.view.RequestViewMethod;

/**
 * Handler of request for view method.
 */
public interface ViewMethodRequestHandler {
    /**
     * Handle the given request of a view method.
     * @param req Request to be handled.
     */
    void handle(RequestViewMethod req);

    /**
     * Handle a disconnect request.
     * @param req Disconnect request.
     */
    void handle(DisconnectRequest req);
}
