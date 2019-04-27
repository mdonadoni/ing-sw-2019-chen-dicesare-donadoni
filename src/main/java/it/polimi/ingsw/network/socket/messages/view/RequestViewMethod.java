package it.polimi.ingsw.network.socket.messages.view;

import it.polimi.ingsw.network.LocalView;
import it.polimi.ingsw.network.socket.messages.Message;
import it.polimi.ingsw.network.socket.messages.ServerToView;

/**
 * Abstract class that represents a generic request for a view method.
 */
public abstract class RequestViewMethod extends Message implements ServerToView {
    /**
     * Command Pattern. Method used to invoke the view method of this request.
     * @param view Method is invoked on this view
     * @return Response corresponding to this Request
     */
    public abstract ResponseViewMethod invokeOn(LocalView view);
}
