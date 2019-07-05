package it.polimi.ingsw;

import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.util.cliparser.*;
import it.polimi.ingsw.view.ViewBot;
import it.polimi.ingsw.view.cli.ViewCLI;
import it.polimi.ingsw.view.gui.ViewGUI;
import javafx.application.Application;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client launcher
 */
public class ClientLauncher {


    /**
     * The main of the client
     * @param args Parameter on the command line
     */
    public static void main(String[] args) {
        final Option hostname = new Option("hostname", true, "hostname or ip of the client");
        final Option gui = new Option("gui", false, "use graphical user interface");
        final Option cli = new Option("cli", false, "use command line interface");
        final Option rmiBot = new Option("rmiBot", false, "start new rmi bot");
        final Option socketBot = new Option("socketBot", false, "start a new socket bot");
        final Option portBot = new Option("portBot", true, "port to be used by the bot to connect to the server");
        final Option serverBot = new Option("serverBot", true, "server address to be used by the bot");
        final Option debug = new Option("debug", false, "enable debug messages (not recommended while using cli)");
        final Option help = new Option("help", false, "print this helper");

        // Disable logging
        Logger.getLogger("").setLevel(Level.OFF);

        // Add options
        Options options = new Options();
        options.addOption(hostname);
        options.addMutuallyExclusiveOptions(gui, cli, rmiBot, socketBot);
        options.addOption(portBot);
        options.addOption(serverBot);
        options.addOption(debug);
        options.addOption(help);

        // Parse options
        OptionsParser parser = new OptionsParser();
        ParsedOptions parsed;

        // parsed port for the bot
        int portForBot = 0;
        // parsed server for the bot
        String serverForBot = null;
        // parsed hostname
        String hostnameValue = "localhost";
        try {
            parsed = parser.parse(options, args);
            // Print help
            if (parsed.hasOption(help)) {
                HelpPrinter.print(options);
                return;
            }

            if (parsed.hasOption(hostname)) {
                hostnameValue = parsed.getOptionValue(hostname);
            }

            if (parsed.hasOption(debug)) {
                Logger.getLogger("").setLevel(Level.INFO);
            }

            if (parsed.hasOption(rmiBot) || parsed.hasOption(socketBot)) {
                if (!parsed.hasOption(portBot)) {
                    HelpPrinter.printWithError(options, "To start a bot --portBot is needed");
                    return;
                } else if (!parsed.hasOption(serverBot)) {
                    HelpPrinter.printWithError(options, "To start a bot --serverBot is needed");
                    return;
                } else {
                    portForBot = Integer.parseInt(parsed.getOptionValue(portBot));
                    serverForBot = parsed.getOptionValue(serverBot);
                }
            }
        } catch (Exception e) {
            HelpPrinter.printWithError(options, e.getMessage());
            return;
        }

        // This is needed to make RMI work
        System.setProperty("java.rmi.server.hostname", hostnameValue);

        // start the right thing
        if (parsed.hasOption(gui)) {
            Application.launch(ViewGUI.class);
        } else if (parsed.hasOption(cli)) {
            new ViewCLI().run();
        } else if (parsed.hasOption(rmiBot)) {
            new ViewBot(serverForBot, portForBot, ConnectionType.RMI).run();
        } else if (parsed.hasOption(socketBot)) {
            new ViewBot(serverForBot, portForBot, ConnectionType.SOCKET).run();
        } else {
            HelpPrinter.printWithError(options, "One of --gui, --cli, --rmiBot, --socketBot is needed");
        }
    }
}
