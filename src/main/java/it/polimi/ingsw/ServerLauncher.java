package it.polimi.ingsw;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.network.LocalServer;

import java.util.logging.Level;
import java.util.logging.Logger;

import static it.polimi.ingsw.network.Constants.RMI_PORT;

public class ServerLauncher {
    private static final Logger LOG = Logger.getLogger(ServerLauncher.class.getName());

    public static void main(String[] args) {
        LocalServer server = new ServerController();
        try {
            LOG.info("Starting RMI Server");
            server.startRMI("0.0.0.0", RMI_PORT);
            LOG.info("RMI Server Started");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Cannot start RMI server", e);
        }
    }
}
