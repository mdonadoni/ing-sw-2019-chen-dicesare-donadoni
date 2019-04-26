package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.NicknameAlreadyUsedException;

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
     * @param host Host of the server.
     * @param port Port of the RMI registry.
     * @throws RemoteException If there is a network error.
     */
    public void startRMI(String host, int port) throws RemoteException {
        LocateRegistry.createRegistry(port);
        Registry registry = LocateRegistry.getRegistry();
        LOG.info("RMI registry started");

        Server stub = (Server) UnicastRemoteObject.exportObject(this, 0);
        LOG.info("RMI Server exported");

        registry.rebind("Server", stub);
        LOG.info("RMI Server bound to registry");
    }

    /**
     * Handle the login of a player.
     * @param nickname Nickname chosen by the player.
     * @param view View of the player.
     * @throws NicknameAlreadyUsedException If the nickname is already used.
     */
    @Override
    public abstract void login(String nickname, View view) throws NicknameAlreadyUsedException;
}
