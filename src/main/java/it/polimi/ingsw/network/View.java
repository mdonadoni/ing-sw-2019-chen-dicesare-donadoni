package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Coordinate;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Interface that represents the methods of the view.
 */
public interface View extends Remote {
    /**
     * Select a square from a list of squares.
     * @param squares Coordinates of the squares.
     * @param min Minimum number of squares to be chosen.
     * @param max Maximum number of squares to be chosen.
     * @return List of selected squares' coordinates.
     * @throws RemoteException If there is a network error.
     */
    List<Coordinate> selectSquares(List<Coordinate> squares, int min, int max) throws RemoteException;
    
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