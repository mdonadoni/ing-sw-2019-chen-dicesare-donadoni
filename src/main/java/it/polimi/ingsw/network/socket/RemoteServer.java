package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.network.LocalView;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.View;
import it.polimi.ingsw.network.socket.messages.Message;
import it.polimi.ingsw.network.socket.messages.server.LoginRequest;
import it.polimi.ingsw.network.socket.messages.server.LoginResponse;
import it.polimi.ingsw.network.socket.messages.view.DisconnectRequest;
import it.polimi.ingsw.network.socket.messages.view.RequestViewMethod;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that represents a RemoteServer. It also acts as a socket endpoint,
 * handling the requests/responses on the view side.
 */
public class RemoteServer implements Server, ViewMethodRequestHandler, Runnable{
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(RemoteServer.class.getName());

    /**
     * View used to invoke the method coming from the requests.
     */
    private final LocalView view;

    /**
     * Socket endpoint.
     */
    private final SocketEndpoint<RequestViewMethod> endpoint;

    /**
     * Constructor of RemoteServer
     * @param view View where the methods will be invoked.
     * @param socket Socket used to create the socket endpoint.
     * @throws IOException If there are errors while creating the endpoint.
     */
    public RemoteServer(LocalView view, Socket socket) throws IOException {
        this.view = view;
        this.endpoint = new SocketEndpoint<>(
                socket,
                RequestViewMethod.class,
                (req) -> req.visit(this));
    }

    /**
     * Request a login on the server.
     * @param nickname Nickname chosen by the player.
     * @param view View of the player.
     * @return True if login is successful, false otherwise.
     * @throws RemoteException If the method cannot be invoked.
     */
    @Override
    public boolean login(String nickname, View view) throws RemoteException{
            LoginResponse res = endpoint.sendAndWaitResponse(new LoginRequest(nickname), LoginResponse.class);
            return res.getResult();
    }

    /**
     * Handler of requests of view methods.
     * @param req Request to be handled.
     */
    @Override
    public void handle(RequestViewMethod req) {
        LOG.log(Level.FINE, "Handling {0}", req);
        Message res = req.invokeOn(view);
        if (res != null) {
            try {
                endpoint.send(res);
            } catch (IOException e) {
                LOG.log(Level.SEVERE, e, () -> "Couldn't handle " + req);
            }
        }
    }

    /**
     * Handler of a disconnect request.
     * @param req Disconnect request.
     */
    @Override
    public void handle(DisconnectRequest req) {
        // handle request (invocation + void response)
        handle((RequestViewMethod) req);
        // close socket
        try {
            endpoint.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Couldn't close endpoint", e);
        }
    }

    /**
     * Start the underlying socket endpoint.
     */
    @Override
    public void run() {
        endpoint.run();
    }
}
