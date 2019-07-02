package it.polimi.ingsw;

import it.polimi.ingsw.util.cliparser.*;
import it.polimi.ingsw.common.ClientConfig;
import it.polimi.ingsw.view.ViewBot;
import it.polimi.ingsw.view.cli.ViewCLI;
import it.polimi.ingsw.view.gui.ViewGUI;
import javafx.application.Application;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientLauncher {



    public static void main(String[] args) {
        final Option hostname = new Option("hostname", true, "hostname or ip of the client");
        final Option gui = new Option("gui", false, "use graphical user interface");
        final Option cli = new Option("cli", false, "use command line interface");
        final Option bot = new Option("bot", false, "start new bot");
        final Option debug = new Option("debug", false, "enable debug messages (not recommended while using cli)");
        final Option help = new Option("help", false, "print this helper");

        // Disable logging
        Logger.getLogger("").setLevel(Level.OFF);

        // Add options
        Options options = new Options();
        options.addOption(hostname);
        options.addMutuallyExclusiveOptions(gui, cli, bot);
        options.addOption(debug);
        options.addOption(help);

        // Parse options
        OptionsParser parser = new OptionsParser();
        ParsedOptions parsed;
        try {
            parsed = parser.parse(options, args);
            // Print help
            if (parsed.hasOption(help)) {
                HelpPrinter.print(options);
                return;
            }

            if (parsed.hasOption(hostname)) {
                ClientConfig.parseHostname(parsed.getOptionValue(hostname));
            }
        } catch (Exception e) {
            HelpPrinter.printWithError(options, e.getMessage());
            return;
        }

        if (parsed.hasOption(debug)) {
            Logger.getLogger("").setLevel(Level.INFO);
        }

        // This is needed to make RMI work
        System.setProperty("java.rmi.server.hostname", ClientConfig.getHostname());

        if (parsed.hasOption(gui)) {
            Application.launch(ViewGUI.class);
        } else if (parsed.hasOption(cli)) {
            new ViewCLI().run();
        } else if (parsed.hasOption(bot)) {
            new ViewBot().run();
        } else {
            HelpPrinter.print(options);
        }
    }
}
