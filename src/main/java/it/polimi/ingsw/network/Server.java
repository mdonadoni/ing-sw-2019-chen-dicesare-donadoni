package it.polimi.ingsw.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface that represents the methods of the server.
 */
public interface Server extends Remote {
    /**
     * Login new player.
     * @param nickname Nickname chosen by the player.
     * @param view View of the player.
     * @return result of the login.
     * @throws RemoteException If there is a network error.
     */
    boolean login(String nickname, View view) throws RemoteException;
}
