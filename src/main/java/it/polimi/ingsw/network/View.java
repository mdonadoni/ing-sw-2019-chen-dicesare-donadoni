package it.polimi.ingsw.network;

import it.polimi.ingsw.common.StandingsItem;
import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.model.minified.MiniModel;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Interface that represents the methods of the view.
 */
public interface View extends Remote {
    /**
     * Select a square from a list of squares.
     * @param objUuid List of the UUID of the objects.
     * @param min Minimum number of objects to be chosen.
     * @param max Maximum number of objects to be chosen.
     * @param dialog The dialog to be shown
     * @return List of selected objects.
     * @throws RemoteException If there is a network error.
     */
    ArrayList<String> selectObject(ArrayList<String> objUuid, int min, int max, Dialog dialog) throws RemoteException;

    /**
     * Show a message on the view.
     * @param message Massage to be shown.
     * @throws RemoteException If there is a network error.
     */
    void showMessage(String message) throws RemoteException;

    /**
     * Update the model of the view.
     * @param model Updated model.
     * @throws RemoteException If there is a network error.
     */
    void updateModel(MiniModel model) throws RemoteException;

    /**
     * Notify the end of a game and send the standings.
     * @param standings Final Standings of the game.
     * @throws RemoteException If there is a network error.
     */
    void notifyEndMatch(ArrayList<StandingsItem> standings) throws RemoteException;

    /**
     * No-op method used to establish if connection is still up.
     * @throws RemoteException If there is a network error.
     */
    void ping() throws RemoteException;

    /**
     * Signal the disconnection of the view.
     * @throws RemoteException If there is a network error.
     */
    void disconnect() throws RemoteException;
}