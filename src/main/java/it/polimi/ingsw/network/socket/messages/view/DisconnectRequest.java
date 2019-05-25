package it.polimi.ingsw.network.socket.messages.view;

import it.polimi.ingsw.network.LocalView;
import it.polimi.ingsw.network.socket.ViewMethodRequestHandler;
import it.polimi.ingsw.network.socket.messages.Message;

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
    public Message invokeOn(LocalView view) {
        view.disconnect();
        return null;
    }

    /**
     * Visitor pattern. DisconnectRequest needs to be handled in a special way.
     * @param handler Handler of a disconnect request.
     */
    @Override
    public void visit(ViewMethodRequestHandler handler) {
        handler.handle(this);
    }
}
