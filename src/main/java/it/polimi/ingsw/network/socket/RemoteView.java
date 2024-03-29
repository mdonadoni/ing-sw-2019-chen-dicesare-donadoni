package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.common.StandingsItem;
import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.model.minified.MiniModel;
import it.polimi.ingsw.network.LocalServer;
import it.polimi.ingsw.network.View;
import it.polimi.ingsw.network.socket.messages.Message;
import it.polimi.ingsw.network.socket.messages.server.LoginRequest;
import it.polimi.ingsw.network.socket.messages.server.RequestServerMethod;
import it.polimi.ingsw.network.socket.messages.view.*;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that represents a RemoteView. It also acts as a socket endpoint,
 * handling the requests/responses on the server side.
 */
public class RemoteView implements View, ServerMethodRequestHandler, Runnable {
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
    private final SocketEndpoint<RequestServerMethod> endpoint;

    /**
     * Constructor of RemoteView.
     * @param server Server on which the request will be invoked.
     * @param socket Socket to be used for the socket endpoint.
     * @throws IOException If there is an error while creating the socket endpoint.
     */
    public RemoteView(LocalServer server, Socket socket) throws IOException {
        this.server = server;
        this.endpoint = new SocketEndpoint<>(
                socket,
                RequestServerMethod.class,
                req -> req.visit(this));
    }

    /**
     * Send a request and wait for a response. In case of error throw RemoteException.
     * @param msg Message to be sent.
     * @param clazz Class of response.
     * @param <T> Type of response
     * @return Response to request.
     * @throws RemoteException If something goes wrong or the request is interrupted.
     */
    private <T extends Message> T sendAndWait(Message msg, Class<T> clazz) throws RemoteException {
        try {
            return endpoint.sendAndWaitResponse(msg, clazz);
        } catch (IOException e) {
            throw new RemoteException("Error while sendig and waiting request", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RemoteException("Request interrupted", e);
        }
    }

    /**
     * Request to chose squares on the view.
     * @param squares Coordinates of the squares.
     * @param min Minimum number of squares to be chosen.
     * @param max Maximum number of squares to be chosen.
     * @param dialog The dialog type
     * @return List of squares' coordinates.
     * @throws RemoteException If there is an error invoking this method.
     */
    @Override
    public ArrayList<String> selectObject(ArrayList<String> squares, int min, int max, Dialog dialog) throws RemoteException {
        SelectObjectResponse res = sendAndWait(new SelectObjectRequest(squares, min, max, dialog), SelectObjectResponse.class);
        return res.getResult();
    }

    /**
     * Request to show a message on the remote view.
     * @param message Massage to be shown.
     * @throws RemoteException If the method cannot be invoked.
     */
    @Override
    public void showMessage(String message) throws RemoteException {
        sendAndWait(new ShowMessageRequest(message), VoidResponse.class);
    }

    /**
     * Request to update the model.
     * @param model Updated model.
     * @throws RemoteException If there is an error while invoking the update.
     */
    @Override
    public void updateModel(MiniModel model) throws RemoteException {
        sendAndWait(new UpdateModelRequest(model), VoidResponse.class);
    }

    /**
     * Request to show the standings and signal the end of a match.
     * @param standings Final Standings of the game.
     * @throws RemoteException If there is an error while invoking the method.
     */
    @Override
    public void notifyEndMatch(ArrayList<StandingsItem> standings) throws RemoteException {
        sendAndWait(new NotifyEndMatchRequest(standings), VoidResponse.class);
    }

    /**
     * Request a ping.
     * @throws RemoteException If there is an error while invoking the ping.
     */
    @Override
    public void ping() throws RemoteException {
        sendAndWait(new PingRequest(), VoidResponse.class);
    }

    /**
     * Request to disconnect the remote view.
     * @throws RemoteException If this method cannot be invoked.
     */
    @Override
    public void disconnect() throws RemoteException {
        try {
            endpoint.send(new DisconnectRequest());
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Couldn't send disconnect request", e);
            throw new RemoteException("Error while disconnecting", e);
        }
    }

    /**
     * Handler of request of server methods.
     * @param req Request to be handled.
     */
    @Override
    public void handle(RequestServerMethod req) {
        LOG.log(Level.FINE, "Handling {0}", req);
        Message res = req.invokeOn(server);
        if (res != null) {
            try {
                endpoint.send(res);
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
     * Start the underlying socket endpoint.
     */
    @Override
    public void run() {
        endpoint.run();
    }
}
