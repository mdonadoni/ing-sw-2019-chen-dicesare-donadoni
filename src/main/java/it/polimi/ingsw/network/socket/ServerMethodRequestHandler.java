package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.network.socket.messages.server.LoginRequest;
import it.polimi.ingsw.network.socket.messages.server.RequestServerMethod;

/**
 * Handler of requests of server methods.
 */
public interface ServerMethodRequestHandler {
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
}
