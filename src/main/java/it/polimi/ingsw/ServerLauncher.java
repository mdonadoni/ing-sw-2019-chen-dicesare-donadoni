package it.polimi.ingsw;

import it.polimi.ingsw.common.ServerConfig;
import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.network.LocalServer;
import it.polimi.ingsw.util.cliparser.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerLauncher {
    private static final Logger LOG = Logger.getLogger(ServerLauncher.class.getName());




    public static void main(String[] args) {
        final Option hostname = new Option("hostname", true, "hostname or ip of the server");
        final Option lobbyTimeout = new Option("lobbyTimeout", true, "timeout before starting lobby in seconds");
        final Option turnTimeout = new Option("turnTimeout", true, "timeout for a single turn of the game in seconds");
        final Option skulls = new Option("skulls", true, "number of skulls in a game");
        final Option help = new Option("help", false, "print this helper");
        final Option rmiPort = new Option("rmiPort", true, "rmi port");
        final Option socketPort = new Option("socketPort", true, "socket port");


        final Options options = new Options();
        options.addOption(hostname);
        options.addOption(lobbyTimeout);
        options.addOption(turnTimeout);
        options.addOption(skulls);
        options.addOption(help);
        options.addOption(rmiPort);
        options.addOption(socketPort);

        // Parse options
        OptionsParser parser = new OptionsParser();
        ParsedOptions parsed;
        try {
            parsed = parser.parse(options, args);

            if (parsed.hasOption(help)) {
                // Print help and return
                HelpPrinter.print(options);
                return;
            }

            if (parsed.hasOption(hostname)) {
                ServerConfig.parseHostname(parsed.getOptionValue(hostname));
            }

            if (parsed.hasOption(lobbyTimeout)) {
                ServerConfig.parseLobbyTimeout(parsed.getOptionValue(lobbyTimeout));
            }

            if (parsed.hasOption(turnTimeout)) {
                ServerConfig.parseTurnTimeout(parsed.getOptionValue(turnTimeout));
            }

            if (parsed.hasOption(skulls)) {
                ServerConfig.parseSkulls(parsed.getOptionValue(skulls));
            }

            if (parsed.hasOption(rmiPort)) {
                ServerConfig.parseRmiPort(parsed.getOptionValue(rmiPort));
            }

            if (parsed.hasOption(socketPort)) {
                ServerConfig.parseSocketPort(parsed.getOptionValue(socketPort));
            }

        } catch (Exception e) {
            HelpPrinter.printWithError(options, e.getMessage());
            return;
        }

        // Make RMI work
        System.setProperty("java.rmi.server.hostname", ServerConfig.getHostname());

        LocalServer server = new ServerController();
        try {
            LOG.info("Starting RMI Server");
            server.startRMI(ServerConfig.getRmiPort());
            LOG.info("RMI Server Started");
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Cannot start RMI server", e);
        }

        try {
            LOG.info("Starting Socket Server");
            server.startSocket(ServerConfig.getSocketPort());
            LOG.info("Socket Server Started");
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Cannot start Socket server", e);
        }
    }
}
