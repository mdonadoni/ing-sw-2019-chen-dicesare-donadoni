package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.NicknameAlreadyUsedException;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class represents a local view.
 */
public abstract class LocalView implements View {
    /**
     * Reference to the RMI stub of the view.
     */
    private View viewStub = null;

    /**
     * Connect to the server using RMI.
     * @param nickname Nickname chosen by the user
     * @param address Address of the server.
     * @param port Port of the RMI registry on the server.
     * @throws RemoteException If there is a network error.
     * @throws NicknameAlreadyUsedException If the nickname is already used.
     */
    protected void connectServerRMI(String nickname, String address, int port) throws RemoteException, NicknameAlreadyUsedException {
        if (viewStub == null) {
            viewStub = (View) UnicastRemoteObject.exportObject(this, 0);
        }

        Registry registry = LocateRegistry.getRegistry(address, port);
        try {
            Server serverStub = (Server) registry.lookup("Server");
            serverStub.login(nickname, viewStub);
        } catch(NotBoundException e) {
            throw new RemoteException("Server not bound to registry", e);
        }
    }

    /**
     * Method to start the local view.
     * @param connection Type of connection to be used
     * @param address Address of the server.
     * @param port Port to connect to the server.
     */
    public abstract void start(ConnectionType connection, String address, int port);

    /**
     * Show a message. This method doesn't throw RemoteException because this
     * is a local view.
     * @param message Massage to be shown.
     */
    @Override
    public abstract void showMessage(String message);

    /**
     * Signal a disconnection from the server.
     */
    @Override
    public void disconnect() throws RemoteException {
        if (viewStub != null) {
            try {
                UnicastRemoteObject.unexportObject(this, true);
            } catch (NoSuchObjectException e) {
                throw new RemoteException("Cannot unexport object", e);
            }
        }
    }
}
