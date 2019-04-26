package it.polimi.ingsw;

import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.network.LocalView;
import it.polimi.ingsw.view.ViewCLI;

import static it.polimi.ingsw.network.Constants.RMI_PORT;

public class ClientLauncher {
    public static void main(String[] args) {
        LocalView view = new ViewCLI();
        try {
            view.start(ConnectionType.RMI, "127.0.0.1", RMI_PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
