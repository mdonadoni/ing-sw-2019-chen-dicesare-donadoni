package it.polimi.ingsw.network;

import it.polimi.ingsw.model.minified.MiniModel;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents a local view.
 */
public abstract class LocalView implements View {

    private static final Logger LOG = Logger.getLogger(LocalView.class.getName());

    private ServerConnection connection;

    protected LocalView() {
    }

    /**
     * Make new connection to the server.
     * @param address Address of the server.
     * @param port Port of the server.
     * @param type Type of connection.
     * @throws RemoteException If there is a network error.
     */
    protected void connectServer(String address, int port, ConnectionType type) throws IOException {
        if (connection != null) {
            connection.close();
            connection = null;
        }

        if (type == ConnectionType.RMI) {
            connection = new ServerConnectionRMI(address, port, this);
        } else {
            connection = new ServerConnectionSocket(address, port, this);
        }
    }

    /** Method to get a reference to the remote server. This method can be
     * called only after creating a successful connection either using RMI
     * or sockets.
     * @return Remote server.
     */
    protected Server getServer() {
        return connection.getServer();
    }

    /**
     * Select a square from a list of squares.
     * @param objUuid Coordinates of the squares.
     * @param min Minimum number of squares to be chosen.
     * @param max Maximum number of squares to be chosen.
     * @return List of selected squares' coordinates.
     */
    @Override
    public abstract List<String> selectObject(List<String> objUuid, int min, int max);

    /**
     * Show a message. This method doesn't throw RemoteException because this
     * is a local view.
     * @param message Massage to be shown.
     */
    @Override
    public abstract void showMessage(String message);

    /**
     * Method used to update the model.
     * @param model Updated model.
     */
    @Override
    public abstract void updateModel(MiniModel model);

    /**
     * Method used to check if connection is up. Shouldn't do anything.
     */
    @Override
    public abstract void ping();

    /**
     * Signal a disconnection from the server. This method doesn't throw RemoteException because this
     * is a local view.
     */
    @Override
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                LOG.log(Level.WARNING, "Couldn't close connection");
            }
        }
    }
}
