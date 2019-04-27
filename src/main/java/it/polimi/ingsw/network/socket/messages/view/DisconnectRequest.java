package it.polimi.ingsw.network.socket.messages.view;

import it.polimi.ingsw.network.LocalView;
import it.polimi.ingsw.network.socket.ViewSideHandler;

/**
 * Request to disconnect the view.
 */
public class DisconnectRequest extends RequestViewMethod {

    /**
     * Command Pattern. Invoke the disconnection on the given view.
     * @param view view to be disconnected.
     * @return null (this request has no response).
     */
    @Override
    public ResponseViewMethod invokeOn(LocalView view) {
        view.disconnect();
        return null;
    }

    /**
     * Visitor Pattern. Let the ViewSideHandler handle this request.
     * @param handler Handler to be used to handle this request.
     */
    @Override
    public void visit(ViewSideHandler handler) {
        handler.handle(this);
    }

    @Override
    public String toString() {
        return "[" + getUUID() + "] DisconnectRequest";
    }
}
