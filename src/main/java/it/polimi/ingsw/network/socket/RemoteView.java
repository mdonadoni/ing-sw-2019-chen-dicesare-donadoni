package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.network.LocalServer;
import it.polimi.ingsw.network.View;
import it.polimi.ingsw.network.socket.messages.*;
import it.polimi.ingsw.network.socket.messages.server.LoginRequest;
import it.polimi.ingsw.network.socket.messages.server.RequestServerMethod;
import it.polimi.ingsw.network.socket.messages.server.ResponseServerMethod;
import it.polimi.ingsw.network.socket.messages.view.DisconnectRequest;
import it.polimi.ingsw.network.socket.messages.view.ResponseViewMethod;
import it.polimi.ingsw.network.socket.messages.view.ShowMessageRequest;
import it.polimi.ingsw.util.BlockingMap;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that represents a RemoteView. It also acts as a socket endpoint,
 * handling the requests/responses on the server side.
 */
public class RemoteView implements View, ServerSideHandler {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(RemoteView.class.getName());

    /**
     * Server where requests will be invoked.
     */
    private final LocalServer server;

    /**
     * Socket endpoint.
     */
    private final SocketEndpoint<ViewToServer, ServerToView> endpoint;

    /**
     * Blocking map of responses. This is used to wait for a response while
     * handling a request.
     */
    private final BlockingMap<String, ResponseViewMethod> responses;

    /**
     * Constructor of RemoteView.
     * @param server Server on which the request will be invoked.
     * @param socket Socket to be used for the socket endpoint.
     * @throws IOException If there is an error while creating the socket endpoint.
     */
    public RemoteView(LocalServer server, Socket socket) throws IOException {
        this.server = server;
        this.endpoint = new SocketEndpoint<>(socket, ViewToServer.class);
        this.responses = new BlockingMap<>();
    }

    /**
     * Private method to log and send messages.
     * @param msg Message to be sent.
     * @throws IOException If there is an error while sending the message.
     */
    private void send(ServerToView msg) throws IOException {
        LOG.log(Level.INFO, "Sending {0}", msg);
        endpoint.send(msg);
        LOG.info("Message to view sent");
    }

    /**
     * Request to show a message on the remote view.
     * @param message Massage to be shown.
     * @throws RemoteException If the method cannot be invoked.
     */
    @Override
    public void showMessage(String message) throws RemoteException {
        ShowMessageRequest req = new ShowMessageRequest(message);
        try {
            send(req);
        } catch (IOException e) {
            throw new RemoteException("Couldn't send request", e);
        }
    }

    /**
     * Request to disconnect the remote view.
     * @throws RemoteException If this method cannot be invoked.
     */
    @Override
    public void disconnect() throws RemoteException {
        DisconnectRequest req = new DisconnectRequest();
        try {
            send(req);
        } catch (IOException e) {
            throw new RemoteException("Couldn't send request", e);
        }
    }

    /**
     * Handler of request of server methods.
     * @param req Request to be handled.
     */
    @Override
    public void handle(RequestServerMethod req) {
        LOG.log(Level.INFO, "Handling {0}", req);
        ResponseServerMethod res = req.invokeOn(server);
        if (res != null) {
            try {
                send(res);
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Couldn't send response", e);
            }
        }

    }

    /**
     * Special handler for login requests. For this request we need to
     * inject the "remote view" on the request.
     * @param req LoginRequest to be handled.
     */
    @Override
    public void handle(LoginRequest req) {
        req.setView(this);
        handle((RequestServerMethod) req);
    }

    /**
     * Handler of responses from view methods.
     * @param res Response to be handled.
     */
    @Override
    public void handle(ResponseViewMethod res) {
        LOG.log(Level.INFO, "Handling {0}", res);
        responses.put(res.getUUID(), res);
    }

    /**
     * Accept messages coming from the remote view, then handle them in
     * separate threads.
     */
    @Override
    public void run() {
        ExecutorService executor = Executors.newCachedThreadPool();

        LOG.info("Starting new RemoteView");
        boolean error = false;
        while (!error && !Thread.interrupted()) {
            try {
                ViewToServer vs = endpoint.receive();
                executor.submit(() -> vs.visit(this));
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Couldn't read from socket", e);
                error = true;
            }
        }
        executor.shutdown();
    }
}
