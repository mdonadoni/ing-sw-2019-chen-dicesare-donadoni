package it.polimi.ingsw.network;

import it.polimi.ingsw.network.socket.SocketListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

/**
 * This class represents a local server.
 */
public abstract class LocalServer implements Server {
    /**
     * Logger of LocalServer.
     */
    private static final Logger LOG = Logger.getLogger(LocalServer.class.getName());

    /**
     * Start RMI server.
     * @param port Port of the RMI registry.
     * @throws RemoteException If there is a network error.
     */
    public void startRMI(int port) throws IOException {
        LocateRegistry.createRegistry(port);
        Registry registry = LocateRegistry.getRegistry();
        LOG.info("RMI registry started");

        Server stub = (Server) UnicastRemoteObject.exportObject(this, 0);
        LOG.info("RMI Server exported");

        registry.rebind("Server", stub);
        LOG.info("RMI Server bound to registry");
    }

    /**
     * Start Socket listener.
     * @param port Listening port.
     * @throws IOException If the listener cannot be started.
     */
    public void startSocket(int port) throws IOException{
        ServerSocket serverSocket = new ServerSocket(port);
        new Thread(new SocketListener(this, serverSocket)).start();
    }

    /**
     * Handle the login of a player.
     * @param nickname Nickname chosen by the player.
     * @param view View of the player.
     */
    @Override
    public abstract boolean login(String nickname, View view);
}
