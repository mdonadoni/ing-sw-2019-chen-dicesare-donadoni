package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.NicknameAlreadyUsedException;

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
     * @throws RemoteException If there is a network error.
     * @throws NicknameAlreadyUsedException If the nickname is already in use.
     */
    void login(String nickname, View view) throws RemoteException, NicknameAlreadyUsedException;
}