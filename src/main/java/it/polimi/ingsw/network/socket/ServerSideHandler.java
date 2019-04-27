package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.network.socket.messages.server.LoginRequest;
import it.polimi.ingsw.network.socket.messages.server.RequestServerMethod;
import it.polimi.ingsw.network.socket.messages.view.ResponseViewMethod;

/**
 * Handler of messages on the server "side".
 * This handler needs to handle both:
 *  - Request of a server method
 *  - Response from a view method.
 */
public interface ServerSideHandler extends Runnable {
    /**
     * Handle the given request of a server method.
     * @param req Request to be handled.
     */
    void handle(RequestServerMethod req);

    /**
     * LoginRequest needs to be handled in different way.
     * @param req LoginRequest to be handled.
     */
    void handle(LoginRequest req);

    /**
     * Handle the given response from a view method.
     * @param res Response to be handled.
     */
    void handle(ResponseViewMethod res);
}
