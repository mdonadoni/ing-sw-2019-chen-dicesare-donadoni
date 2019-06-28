package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.model.minified.MiniModel;
import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.network.LocalView;
import it.polimi.ingsw.view.Descriptions;
import it.polimi.ingsw.view.cli.component.ModelCLI;

import java.io.IOException;
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
                System.out.println("Connessione [rmi/socket]:");
                connection = scanner.nextLine();
            }

            ConnectionType type;
            if (connection.equals("rmi")) {
                type = ConnectionType.RMI;
            } else {
                type = ConnectionType.SOCKET;
            }

            System.out.println("Indirizzo server:");
            String address = scanner.nextLine();

            System.out.println("Porta server:");
            int port = Integer.parseInt(scanner.nextLine());

            System.out.println("Scegli username: ");
            String nickname = scanner.nextLine();

            try {
                connectServer(address, port, type);
                if (!getServer().login(nickname, this)) {
                    System.out.println("Username già utilizzato o non valido!");
                    connected = false;
                }
            } catch (IOException e) {
                LOG.log(Level.WARNING, "Couldn't connect to server", e);
                System.out.println("Impossibile connettersi");
                connected = false;
            }
        }
    }

    @Override
    public synchronized ArrayList<String> selectObject(ArrayList<String> objUuid, int min, int max, Dialog dialog) {
        System.out.println("Nuova selezione");
        for (int i = 0; i < objUuid.size(); i++) {
            System.out.println(i + ") " + Descriptions.find(model, objUuid.get(i)));
        }

        while (true) {
            System.out.println("Scegli da " + min + " a " + max + " elementi");
            String res = scanner.nextLine();
            String[] selection =  res.split("\\s+");
            Set<String> uuid = new HashSet<>();
            try {
                for (String s : selection) {
                    if (s.length() == 0) {
                        continue;
                    }
                    int idx = Integer.parseInt(s);
                    if (idx < 0 || idx >= objUuid.size()) {
                        throw new RuntimeException("Invalid selection");
                    }
                    if (uuid.contains(objUuid.get(idx))) {
                        throw new RuntimeException("uuid already chosen");
                    }
                    uuid.add(objUuid.get(idx));
                }
            } catch (Exception e) {
                continue;
            }

            if (uuid.size() >= min && uuid.size() <= max) {
                return new ArrayList<>(uuid);
            }
        }
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public synchronized void updateModel(MiniModel model) {
        this.model = model;
        ModelCLI modelCLI = new ModelCLI(model);
        cls();
        List<String> view = (List<String>) modelCLI.viewModel();
        for (String s : view) {
            System.out.println(s);
        }
    }

    private void cls() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    @Override
    public void disconnect() {
        super.disconnect();
        System.out.println("Disconnected from server");
    }
}
