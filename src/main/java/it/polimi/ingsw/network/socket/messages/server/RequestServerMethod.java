package it.polimi.ingsw.network.socket.messages.server;

import it.polimi.ingsw.network.LocalServer;
import it.polimi.ingsw.network.socket.ServerSideHandler;
import it.polimi.ingsw.network.socket.messages.ViewToServer;

/**
 * Abstract class that represents a generic request of a server method.
 */
public abstract class RequestServerMethod extends ViewToServer {
    /**
     * Command Pattern. Method used to invoke the server method of this request.
     * @param server Method is invoked on this server
     * @return Response corresponding to this Request
     */
    public abstract ResponseServerMethod invokeOn(LocalServer server);

    /**
     * Mathod used to handle a server method request (Visitor Pattern).
     * @param handler Handler of a server method request.
     */
    @Override
    public void visit(ServerSideHandler handler) {
        handler.handle(this);
    }
}
