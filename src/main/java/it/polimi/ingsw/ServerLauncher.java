package it.polimi.ingsw;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.network.LocalServer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.polimi.ingsw.network.Constants.RMI_PORT;
import static it.polimi.ingsw.network.Constants.SOCKET_PORT;

public class ServerLauncher {
    private static final Logger LOG = Logger.getLogger(ServerLauncher.class.getName());

    public static void main(String[] args) {
        LocalServer server = new ServerController();
        try {
            LOG.info("Starting RMI Server");
            server.startRMI(RMI_PORT);
            LOG.info("RMI Server Started");
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Cannot start RMI server", e);
        }

        try {
            LOG.info("Starting Socket Server");
            server.startSocket(SOCKET_PORT);
            LOG.info("Socket Server Started");
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Cannot start Socket server", e);
        }
    }
}
