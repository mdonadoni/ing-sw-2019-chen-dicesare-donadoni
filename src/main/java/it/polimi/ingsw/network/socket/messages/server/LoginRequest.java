package it.polimi.ingsw.network.socket.messages.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.network.LocalServer;
import it.polimi.ingsw.network.View;
import it.polimi.ingsw.network.socket.ServerSideHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Request for method login of server.
 */
public class LoginRequest extends RequestServerMethod {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(LoginRequest.class.getName());

    /**
     * Nickname chosen by the player.
     */
    private String nickname;

    /**
     * Reference to the view.
     */
    private View view;

    /**
     * Constructor of LoginRequest.
     * @param nickname Nickname of the player
     */
    @JsonCreator
    public LoginRequest(@JsonProperty("nickname") String nickname) {
        this.nickname = nickname;
        this.view = null;
    }

    /**
     * Method to set the view. We need this method because, to mimic the RMI
     * protocol, we need to pass to the server a reference to a "remote" view.
     * In order to do so, we need to inject the RemoteView from the server side.
     * @param view Reference to the view.
     */
    public void setView(View view) {
        this.view = view;
    }

    /**
     * Invoke the login action on the server.
     * @param server Server to login.
     * @return Response of login.
     */
    @Override
    public ResponseServerMethod invokeOn(LocalServer server) {
        LOG.log(Level.INFO, "Invoking login for {0}", nickname);
        return new LoginResponse(this, server.login(nickname, view));
    }

    /**
     * Method used to handle a server method request (Visitor Pattern).
     * This override is needed because the login request needs to be handled
     * in a different way from other requests.
     * @param handler Handler of a server request.
     */
    @Override
    public void visit(ServerSideHandler handler) {
        handler.handle(this);
    }

    @Override
    public String toString() {
        return "[" + getUUID() + "] LoginRequest for " + nickname;
    }
}
