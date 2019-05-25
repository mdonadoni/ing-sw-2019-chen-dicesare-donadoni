package it.polimi.ingsw;

import it.polimi.ingsw.view.ViewGUI;
import javafx.application.Application;

public class ClientLauncher {
    public static void main(String[] args) {
        //ViewGUI.main(args);
        //Logger.getLogger("").setLevel(Level.FINE);
        //ViewCLI cli = new ViewCLI();
        //cli.run();
        Application.launch(ViewGUI.class);
    }
}
