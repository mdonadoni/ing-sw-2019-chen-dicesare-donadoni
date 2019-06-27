package it.polimi.ingsw;

import it.polimi.ingsw.util.cliparser.*;
import it.polimi.ingsw.util.config.Config;
import it.polimi.ingsw.view.ViewBot;
import it.polimi.ingsw.view.cli.ViewCLI;
import it.polimi.ingsw.view.gui.ViewGUI;
import javafx.application.Application;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientLauncher {

    private static final Option HOSTNAME = new Option("hostname", true, "hostname or ip of the client");
    private static final Option GUI = new Option("gui", false, "use graphical user interface");
    private static final Option CLI = new Option("cli", false, "use command line interface");
    private static final Option BOT = new Option("bot", false, "start new bot");
    private static final Option DEBUG = new Option("debug", false, "enable debug messages (not recommended while using cli)");

    public static void main(String[] args) {
        // Disable logging
        Logger.getLogger("").setLevel(Level.OFF);

        // Add options
        Options options = new Options();
        options.addOption(HOSTNAME);
        options.addMutuallyExclusiveOptions(GUI, CLI, BOT);
        options.addOption(DEBUG);

        // Parse options
        OptionsParser parser = new OptionsParser();
        ParsedOptions parsed;
        try {
            parsed = parser.parse(options, args);

            if (parsed.hasOption(HOSTNAME)) {
                Config.setHostname(parsed.getOptionValue(HOSTNAME));
            }
        } catch (Exception e) {
            HelpPrinter.print(options);
            return;
        }

        if (parsed.hasOption(DEBUG)) {
            Logger.getLogger("").setLevel(Level.INFO);
        }

        // This is needed to make RMI work
        System.setProperty("java.rmi.server.hostname", Config.getHostname());

        if (parsed.hasOption(GUI)) {
            Application.launch(ViewGUI.class);
        } else if (parsed.hasOption(CLI)) {
            new ViewCLI().run();
        } else if (parsed.hasOption(BOT)) {
            new ViewBot().run();
        } else {
            HelpPrinter.print(options);
        }
    }
}
