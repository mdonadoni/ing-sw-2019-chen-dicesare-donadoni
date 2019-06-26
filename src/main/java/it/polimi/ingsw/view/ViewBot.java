package it.polimi.ingsw.view;

import it.polimi.ingsw.model.minified.MiniModel;
import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.network.LocalView;

import java.util.ArrayList;
import java.util.UUID;

public class ViewBot extends LocalView implements Runnable {
    @Override
    public synchronized ArrayList<String> selectObject(ArrayList<String> objUuid, int min, int max) {
        System.out.println("SELECT: " + objUuid);
        return new ArrayList<>(objUuid.subList(0, max));
    }

    @Override
    public void showMessage(String message) {
        System.out.println("MESSAGE: " + message);
    }

    @Override
    public synchronized void updateModel(MiniModel model) {
        System.out.println("NEW MODEL");
    }

    @Override
    public void run() {
        try {
            connectServer("localhost", 9999, ConnectionType.SOCKET);
            String name = UUID.randomUUID().toString().substring(0, 4);
            System.out.println("IO SONO " + name);
            getServer().login(name, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
