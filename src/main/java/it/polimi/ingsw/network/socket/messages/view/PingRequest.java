package it.polimi.ingsw.network.socket.messages.view;

import it.polimi.ingsw.network.LocalView;
import it.polimi.ingsw.network.socket.messages.Message;

/**
 * Request a ping.
 */
public class PingRequest extends RequestViewMethod {
    /**
     * Command pattern. Invoke the ping on given view.
     * @param view Method is invoked on this view
     * @return
     */
    @Override
    public Message invokeOn(LocalView view) {
        view.ping();
        return new VoidResponse(this);
    }
}
