package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.common.StandingsItem;
import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.common.dialogs.Dialogs;
import it.polimi.ingsw.model.minified.MiniModel;
import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.network.LocalView;
import it.polimi.ingsw.view.Descriptions;
import it.polimi.ingsw.view.cli.component.ModelCLI;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewCLI extends LocalView implements Runnable {
    private static final Logger LOG = Logger.getLogger(ViewCLI.class.getName());


    private Scanner scanner;
    private MiniModel model;

    public ViewCLI() {
        scanner = new Scanner(System.in);
    }

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
    }

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

    @Override
    public synchronized void showMessage(String message) {
        println(message);
    }

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

    @Override
    public void notifyEndMatch(ArrayList<StandingsItem> standings) {
        // TODO
    }

    private synchronized String  readLine() {
        return scanner.nextLine();
    }

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

    private synchronized void cls() {
        for (int i = 0; i < 50; i++) {
            println();
        }
    }

    private synchronized void printDialog(Dialog dialog, String ...params) {
        println(Dialogs.getDialog(dialog, params));
    }

    private synchronized void println(String s) {
        System.out.println(s);
    }

    private void println() {
        println("");
    }

    @Override
    public void disconnect() {
        super.disconnect();
        System.out.println("Disconnesso dal server");
        // fix for RMI
        System.exit(0);
    }
}
