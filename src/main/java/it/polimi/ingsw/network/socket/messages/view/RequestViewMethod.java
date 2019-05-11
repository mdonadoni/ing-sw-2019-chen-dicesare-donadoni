package it.polimi.ingsw.network.socket.messages.view;

import it.polimi.ingsw.network.LocalView;
import it.polimi.ingsw.network.socket.ViewMethodRequestHandler;
import it.polimi.ingsw.network.socket.messages.Message;

/**
 * Abstract class that represents a generic request for a view method.
 */
public abstract class RequestViewMethod extends Message {
    /**
     * Command Pattern. Method used to invoke the view method of this request.
     * @param view Method is invoked on this view
     * @return Response corresponding to this Request
     */
    public abstract Message invokeOn(LocalView view);

    /**
     * Method used to handle a view method request (Visitor Pattern).
     * @param handler Handler of a view method request.
     */
    public void visit(ViewMethodRequestHandler handler) {
        handler.handle(this);
    }
}
