package it.polimi.ingsw.network.socket.messages.view;

import it.polimi.ingsw.network.socket.messages.Message;
import it.polimi.ingsw.network.socket.messages.ViewToServer;

/**
 * Abstract class that represents a generic response of a view method.
 */
public abstract class ResponseViewMethod extends Message implements ViewToServer {
    /**
     * Constructor of ResponseViewMethod.
     * @param uuid uuid of the corresponding request.
     */
    ResponseViewMethod(String uuid) {
        super(uuid);
    }
}
