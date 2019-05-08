package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.network.LocalServer;
import it.polimi.ingsw.network.View;
import it.polimi.ingsw.network.socket.messages.ServerToView;
import it.polimi.ingsw.network.socket.messages.ViewToServer;
import it.polimi.ingsw.network.socket.messages.server.LoginRequest;
import it.polimi.ingsw.network.socket.messages.server.RequestServerMethod;
import it.polimi.ingsw.network.socket.messages.server.ResponseServerMethod;
import it.polimi.ingsw.network.socket.messages.view.*;
import it.polimi.ingsw.util.BlockingMap;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
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
     * AtomicBoolean which indicates if the connection is still alive.
     */
    private AtomicBoolean connected;

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
        this.connected = new AtomicBoolean(true);
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
     * Method to send a request and get the response.
     * @param req Request to send.
     * @return Response of the request.
     * @throws RemoteException If an error occurs while sending the request.
     */
    private ResponseViewMethod sendRequest(RequestViewMethod req) throws RemoteException {
        if (!connected.get()) {
            throw new RemoteException("View not connected");
        }
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
     * Request to chose squares on the view.
     * @param squares Coordinates of the squares.
     * @param min Minimum number of squares to be chosen.
     * @param max Maximum number of squares to be chosen.
     * @return List of squares' coordinates.
     * @throws RemoteException If there is an error invoking this method.
     */
    @Override
    public List<String> selectObject(List<String> squares, int min, int max) throws RemoteException {
        try {
            SelectObjectResponse res = (SelectObjectResponse) sendRequest(new SelectObjectRequest(squares, min, max));
            return res.getResult();
        } catch (ClassCastException e) {
            throw new RemoteException("Response is not SelectObjectResponse");
        }
    }

    /**
     * Request to show a message on the remote view.
     * @param message Massage to be shown.
     * @throws RemoteException If the method cannot be invoked.
     */
    @Override
    public void showMessage(String message) throws RemoteException {
        sendRequest(new ShowMessageRequest(message));
    }

    /**
     * Request to disconnect the remote view.
     * @throws RemoteException If this method cannot be invoked.
     */
    @Override
    public void disconnect() throws RemoteException {
        sendRequest(new DisconnectRequest());
        connected.set(false);
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
        LOG.info("Starting new RemoteView");
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            while (connected.get() && !Thread.interrupted()) {
                    ViewToServer vs = endpoint.receive();
                    executor.submit(() -> vs.visit(this));
            }
        } catch (IOException e) {
            if (connected.get()) {
                LOG.log(Level.SEVERE, "Couldn't read from socket", e);
            }
        }
        executor.shutdown();
    }
}
