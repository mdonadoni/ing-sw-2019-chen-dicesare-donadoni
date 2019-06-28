package it.polimi.ingsw.network;

import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.model.minified.MiniModel;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestView implements View {

    public List<String> toBeSelected = new ArrayList<>();

    public ArrayList<String> selectObject(ArrayList<String> objUuid, int min, int max, Dialog dialog) throws RemoteException {
        if(toBeSelected.size()>0){
            String returnVal = toBeSelected.get(0);
            toBeSelected.remove(0);
            if(returnVal!=null)
                return new ArrayList<>(Arrays.asList(returnVal));
            else
                return new ArrayList<>(objUuid.subList(0, min));
        }
        else
            return new ArrayList<>(objUuid.subList(0, min));
    }
    public void showMessage(String message) throws RemoteException{
        // Ehm
    }

    @Override
    public void updateModel(MiniModel model) throws RemoteException {
        // Mhh
    }

    @Override
    public void ping() throws RemoteException {
        // Oof
    }

    public void disconnect() throws RemoteException{
        // Uhm
    }
}
