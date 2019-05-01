package it.polimi.ingsw.network.socket.messages.view;

import it.polimi.ingsw.network.LocalView;
import it.polimi.ingsw.network.socket.ViewSideHandler;
import it.polimi.ingsw.network.socket.messages.ServerToView;

/**
 * Abstract class that represents a generic request for a view method.
 */
public abstract class RequestViewMethod extends ServerToView {
    /**
     * Command Pattern. Method used to invoke the view method of this request.
     * @param view Method is invoked on this view
     * @return Response corresponding to this Request
     */
    public abstract ResponseViewMethod invokeOn(LocalView view);

    /**
     * Mathod used to handle a view method request (Visitor Pattern).
     * @param handler Handler of a view method request.
     */
    @Override
    public void visit(ViewSideHandler handler) {
        handler.handle(this);
    }
}
