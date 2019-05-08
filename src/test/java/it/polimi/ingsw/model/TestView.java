package it.polimi.ingsw.model;

import it.polimi.ingsw.network.View;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class TestView implements View {

    public List<String> selectObject(List<String> objUuid, int min, int max) throws RemoteException {
        return new ArrayList<>(objUuid.subList(0, min));
    }
    public void showMessage(String message) throws RemoteException{
        // Ehm
    }
    public void disconnect() throws RemoteException{
        // Uhm
    }
}
