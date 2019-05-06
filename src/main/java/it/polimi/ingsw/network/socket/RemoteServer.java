package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.network.LocalView;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.View;
import it.polimi.ingsw.network.socket.messages.ServerToView;
import it.polimi.ingsw.network.socket.messages.ViewToServer;
import it.polimi.ingsw.network.socket.messages.server.LoginRequest;
import it.polimi.ingsw.network.socket.messages.server.LoginResponse;
import it.polimi.ingsw.network.socket.messages.server.RequestServerMethod;
import it.polimi.ingsw.network.socket.messages.server.ResponseServerMethod;
import it.polimi.ingsw.network.socket.messages.view.RequestViewMethod;
import it.polimi.ingsw.network.socket.messages.view.ResponseViewMethod;
import it.polimi.ingsw.util.BlockingMap;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that represents a RemoteServer. It also acts as a socket endpoint,
 * handling the requests/responses on the view side.
 */
public class RemoteServer implements Server, ViewSideHandler {
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
    private final SocketEndpoint<ServerToView, ViewToServer> endpoint;

    /**
     * Blocking map of responses. This is used to wait for a response while
     * handling a request.
     */
    private final BlockingMap<String, ResponseServerMethod> responses;

    /**
     * Constructor of RemoteServer
     * @param view View where the methods will be invoked.
     * @param socket Socket used to create the socket endpoint.
     * @throws IOException If there are errors while creating the endpoint.
     */
    public RemoteServer(LocalView view, Socket socket) throws IOException {
        this.view = view;
        this.endpoint = new SocketEndpoint<>(socket, ServerToView.class);
        this.responses = new BlockingMap<>();
    }

    /**
     * Private method to log and send a message.
     * @param msg Message to be sent.
     * @throws IOException If there are errors while sending the message.
     */
    private void send(ViewToServer msg) throws IOException {
        LOG.log(Level.INFO, "Sending {0}", msg);
        endpoint.send(msg);
        LOG.info("Message to server sent");
    }

    /**
     * Method to send a request and get the response.
     * @param req Request to send.
     * @return Response of the request.
     * @throws RemoteException If an error occurs while sending the request.
     */
    private ResponseServerMethod sendRequest(RequestServerMethod req) throws RemoteException {
        try {
            send(req);
            return responses.getAndRemove(req.getUUID());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RemoteException("Request interrupted", e);
        } catch (IOException e) {
            throw new RemoteException("Couldn't send request", e);
        }
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
        try {
            LoginResponse res = (LoginResponse) sendRequest(new LoginRequest(nickname));
            return res.getResult();
        } catch (ClassCastException e) {
            throw new RemoteException("Response is not LoginResponse");
        }
    }

    /**
     * Handler of requests of view methods.
     * @param req Request to be handled.
     */
    @Override
    public void handle(RequestViewMethod req) {
        LOG.log(Level.INFO, "Handling {0}", req);
        ResponseViewMethod res = req.invokeOn(view);
        if (res != null) {
            try {
                send(res);
            } catch (IOException e) {
                LOG.log(Level.SEVERE, e, () -> "Couldn't handle " + req);
            }
        }
    }

    /**
     * Handler of responses from a server method.
     * @param res Response to be handled.
     */
    @Override
    public void handle(ResponseServerMethod res) {
        LOG.log(Level.INFO, "Handling {0}", res);
        responses.put(res.getUUID(), res);
    }

    /**
     * Accept messages coming from the remote server, then handle them in
     * separate threads.
     */
    @Override
    public void run() {
        ExecutorService executor = Executors.newCachedThreadPool();

        boolean error = false;
        while (!error && !Thread.interrupted()) {
            try {
                LOG.info("Reading new message from socket");
                ServerToView wv = endpoint.receive();
                executor.submit(() -> wv.visit(this));
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Error while reading socket", e);
                error = true;
            }
        }
        executor.shutdown();
        LOG.info("Disconnecting");
    }
}
