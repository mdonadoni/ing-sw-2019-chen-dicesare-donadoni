package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.common.StandingsItem;
import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.common.dialogs.Dialogs;
import it.polimi.ingsw.model.minified.MiniModel;
import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.network.LocalView;
import it.polimi.ingsw.view.Descriptions;
import it.polimi.ingsw.view.cli.component.ModelCLI;
import it.polimi.ingsw.view.cli.util.ColorCLI;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The CLI view
 */
public class ViewCLI extends LocalView implements Runnable {
    /**
     * Logger
     */
    private static final Logger LOG = Logger.getLogger(ViewCLI.class.getName());
    /**
     * Standings line
     */
    private static final String STANDINGS_LINE = "{0}) {1} ({2} punti)";
    /**
     * Scanner
     */
    private Scanner scanner;
    /**
     * Model
     */
    private MiniModel model;

    /**
     * Constructor
     */
    public ViewCLI() {
        scanner = new Scanner(System.in);
    }

    /**
     * Run the CLI
     */
    @Override
    public synchronized void run() {
        String connection;
        boolean connected = false;
        while (!connected) {
            connected = true;

            connection = null;
            while (connection == null || (!connection.equals("rmi") && !connection.equals("socket"))) {
                println("Connessione [rmi/socket]:");
                connection = readLine();
            }

            ConnectionType type;
            if (connection.equals("rmi")) {
                type = ConnectionType.RMI;
            } else {
                type = ConnectionType.SOCKET;
            }

            println("Indirizzo server:");
            String address = readLine();

            println("Porta server:");
            int port = readInt();

            println("Scegli username: ");
            String nickname = readLine();

            try {
                connectServer(address, port, type);
                if (!getServer().login(nickname, this)) {
                    println("Username già utilizzato o non valido!");
                    connected = false;
                }
            } catch (IOException e) {
                LOG.log(Level.WARNING, "Couldn't connect to server", e);
                println("Impossibile connettersi");
                connected = false;
            }
        }
        println(Dialogs.getDialog(Dialog.WAIT_MATCH_START));
    }

    /**
     * Select object
     * @param objUuid List of the UUID of the objects.
     * @param min Minimum objects.
     * @param max Maximum of objects.
     * @param dialog The dialog type
     * @return List of selected objects
     */
    @Override
    public synchronized ArrayList<String> selectObject(ArrayList<String> objUuid, int min, int max, Dialog dialog) {
        // Print options
        printDialog(Dialog.NEW_SELECTION);
        for (int i = 0; i < objUuid.size(); i++) {
            println(MessageFormat.format("{0}) {1}", i ,Descriptions.find(model, objUuid.get(i))));
        }

        boolean validSelection = false;
        Set<String> selectedUuid = null;

        while (!validSelection) {
            validSelection = true;
            // Ask what to do
            printDialog(dialog, Integer.toString(min), Integer.toString(max));
            // Split on spaces
            String[] selection = readLine().split("\\s+");
            selectedUuid = new HashSet<>();
            try {
                // For every selection
                for (String s : selection) {
                    if (s.length() == 0) {
                        // ignore empty strings
                        continue;
                    }
                    // parse selection
                    int idx = Integer.parseInt(s);
                    if (idx < 0 || idx >= objUuid.size() || selectedUuid.contains(objUuid.get(idx))) {
                        validSelection = false;
                    } else {
                        selectedUuid.add(objUuid.get(idx));
                    }
                }
            } catch (Exception e) {
                validSelection = false;
            }

            if (!validSelection || selectedUuid.size() < min || selectedUuid.size() > max) {
                printDialog(Dialog.INVALID_SELECTION);
                validSelection = false;
            }
        }

        return new ArrayList<>(selectedUuid);
    }

    /**
     * Show message
     * @param message Massage to be shown.
     */
    @Override
    public synchronized void showMessage(String message) {
        println(message);
    }

    /**
     * Update the model
     * @param model Updated model.
     */
    @Override
    public synchronized void updateModel(MiniModel model) {
        // Save new model
        this.model = model;
        // Create new cli model
        ModelCLI modelCLI = new ModelCLI(model);
        // Clear screen
        cls();
        // Print model
        List<String> view = modelCLI.viewModel();
        for (String s : view) {
            println(s);
        }
    }

    /**
     * Notify the end of the match
     * @param standings Final Standings of the game.
     */
    @Override
    public synchronized void notifyEndMatch(ArrayList<StandingsItem> standings) {
        cls();
        println(Dialogs.getDialog(Dialog.FINAL_STANDINGS));
        for (StandingsItem s : standings) {
            System.out.println(
                    MessageFormat.format(
                            STANDINGS_LINE,
                            s.getPosition(),
                            ColorCLI.getPlayerColor(s.getColor(), s.getNickname()),
                            s.getPoints()));
        }
    }

    /**
     * Read a line
     * @return The next line to read
     */
    private synchronized String  readLine() {
        return scanner.nextLine();
    }

    /**
     * Read int
     * @return read an integer value
     */
    private synchronized int readInt() {
        int integer = 0;
        boolean valid = false;
        while (!valid) {
            valid = true;
            try {
                integer = Integer.parseInt(readLine());
            } catch (NumberFormatException e) {
                valid = false;
            }

            if (!valid) {
                printDialog(Dialog.INVALID_INTEGER);
            }
        }
        return integer;
    }

    /**
     * Clear the screen
     */
    private synchronized void cls() {
        println(ColorCLI.ANSI_CLS);
    }

    /**
     * Print dialog
     * @param dialog The dialog
     * @param params The parameter to fill the dialog
     */
    private synchronized void printDialog(Dialog dialog, String ...params) {
        println(Dialogs.getDialog(dialog, params));
    }

    /**
     * Print a string and go to the next line
     * @param s the string to print
     */
    private synchronized void println(String s) {
        System.out.println(s);
    }

    /**
     * Print no string and go to the next line
     */
    private void println() {
        println("");
    }

    /**
     * Disconnect
     */
    @Override
    public void disconnect() {
        super.disconnect();
        System.out.println("Disconnesso dal server");
        // fix for RMI
        System.exit(0);
    }
}
