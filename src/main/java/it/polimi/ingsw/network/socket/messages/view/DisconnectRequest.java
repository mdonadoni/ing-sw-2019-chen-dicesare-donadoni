package it.polimi.ingsw.network.socket.messages.view;

import it.polimi.ingsw.network.LocalView;

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
}
