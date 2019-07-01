package it.polimi.ingsw;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.network.LocalServer;
import it.polimi.ingsw.util.cliparser.*;
import it.polimi.ingsw.util.config.Config;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.polimi.ingsw.network.Constants.RMI_PORT;
import static it.polimi.ingsw.network.Constants.SOCKET_PORT;

public class ServerLauncher {
    private static final Logger LOG = Logger.getLogger(ServerLauncher.class.getName());

    private static final Option HOSTNAME = new Option("hostname", true, "hostname or ip of the server");
    private static final Option LOBBY_TIMEOUT = new Option("lobbyTimeout", true, "timeout before starting lobby in seconds");
    private static final Option TURN_TIMEOUT = new Option("turnTimeout", true, "timeout for a single turn of the game in seconds");
    private static final Option SKULLS = new Option("skulls", true, "number of skulls in a game");


    public static void main(String[] args) {
        Options options = new Options();
        options.addOption(HOSTNAME);
        options.addOption(LOBBY_TIMEOUT);
        options.addOption(TURN_TIMEOUT);
        options.addOption(SKULLS);

        // Parse options
        OptionsParser parser = new OptionsParser();
        ParsedOptions parsed;
        try {
            parsed = parser.parse(options, args);

            if (parsed.hasOption(HOSTNAME)) {
                Config.parseHostname(parsed.getOptionValue(HOSTNAME));
            }

            if (parsed.hasOption(LOBBY_TIMEOUT)) {
                Config.parseLobbyTimeout(parsed.getOptionValue(LOBBY_TIMEOUT));
            }

            if (parsed.hasOption(TURN_TIMEOUT)) {
                Config.parseTurnTimeout(parsed.getOptionValue(TURN_TIMEOUT));
            }

            if (parsed.hasOption(SKULLS)) {
                Config.parseSkulls(parsed.getOptionValue(SKULLS));
            }
        } catch (Exception e) {
            HelpPrinter.print(options);
            return;
        }

        // Make RMI work
        System.setProperty("java.rmi.server.hostname", Config.getHostname());

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
