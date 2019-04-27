package it.polimi.ingsw.network.socket.messages.server;

import it.polimi.ingsw.network.socket.messages.Message;
import it.polimi.ingsw.network.socket.messages.ServerToView;

/**
 * Abstract class that represents a generic response of a server method.
 */
public abstract class ResponseServerMethod extends Message implements ServerToView {

    /**
     * Constructor of ResponseServerMethod.
     * @param uuid uuid of the corresponding request.
     */
    ResponseServerMethod(String uuid) {
        super(uuid);
    }
}
