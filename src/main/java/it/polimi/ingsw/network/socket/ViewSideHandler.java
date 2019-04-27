package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.network.socket.messages.server.ResponseServerMethod;
import it.polimi.ingsw.network.socket.messages.view.RequestViewMethod;

/**
 * Handler of messages on the view "side".
 * This handler needs to handle both:
 *  - Request of a view method
 *  - Response from a server method.
 */
public interface ViewSideHandler extends Runnable {
    /**
     * Handle the given request of a view method.
     * @param req Request to be handled.
     */
    void handle(RequestViewMethod req);

    /**
     * Handle the given response from a server method.
     * @param res Response to be handled.
     */
    void handle(ResponseServerMethod res);
}
