package it.polimi.ingsw.network;

import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.model.minified.MiniModel;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents a local view.
 */
public abstract class LocalView implements View {
    /**
     * Logger
     */
    private static final Logger LOG = Logger.getLogger(LocalView.class.getName());
    /**
     * Period of the listening of pings from server.
     */
    private static final long PING_LISTEN_PERIOD = 15000;

    /**
     * Connection to the server.
     */
    private ServerConnection connection;
    /**
     * Keeps whether the view has received as least one ping.
     */
    private AtomicBoolean firstPing = new AtomicBoolean(true);
    /**
     * Keeps wheter the view has received one ping since last time the timer checked.
     */
    private AtomicBoolean receivedPing = new AtomicBoolean(false);
    /**
     * Timer to schedule the check for the pings from the server.
     */
    private Timer timer = new Timer();

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
     * @param dialog The dialog type
     * @return List of selected squares' coordinates.
     */
    @Override
    public abstract ArrayList<String> selectObject(ArrayList<String> objUuid, int min, int max, Dialog dialog);

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
     * When the first ping is received, the timer is started.
     */
    @Override
    public void ping() {
        receivedPing.set(true);

        if (firstPing.getAndSet(false)) {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (!receivedPing.getAndSet(false)) {
                        timer.cancel();
                        disconnect();
                    }
                }
            };
            timer.schedule(task, 0, PING_LISTEN_PERIOD);
        }
    }

    /**
     * Signal a disconnection from the server. This method doesn't throw RemoteException because this
     * is a local view.
     */
    @Override
    public void disconnect() {
        closeConnection();
    }

    /**
     * Close the connection to the server and stop the timer.
     */
    public void closeConnection() {
        timer.cancel();
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                LOG.log(Level.WARNING, "Couldn't close connection");
            }
        }
    }
}
