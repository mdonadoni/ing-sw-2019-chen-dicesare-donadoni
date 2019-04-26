package it.polimi.ingsw.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface that represents the methods of the view.
 */
public interface View extends Remote {
    /**
     * Show a message on the view.
     * @param message Massage to be shown.
     * @throws RemoteException If there is a network error.
     */
    void showMessage(String message) throws RemoteException;

    /**
     * Signal the disconnection of the view.
     * @throws RemoteException If there is a network error.
     */
    void disconnect() throws RemoteException;
}