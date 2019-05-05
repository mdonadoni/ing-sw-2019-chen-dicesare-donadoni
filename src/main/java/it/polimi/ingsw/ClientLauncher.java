package it.polimi.ingsw;

import it.polimi.ingsw.network.LocalView;
import it.polimi.ingsw.view.ViewCLI;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientLauncher {
    public static void main(String[] args) {
        Logger.getLogger("").setLevel(Level.OFF);
        try {
            LocalView view = new ViewCLI();
            view.run();
        } catch (RemoteException e) {
            System.out.println("Cannot start view");
        }
    }
}
