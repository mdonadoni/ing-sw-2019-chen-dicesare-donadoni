package it.polimi.ingsw.view;

import it.polimi.ingsw.model.minified.MiniModel;
import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.network.LocalView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewCLI extends LocalView implements Runnable {
    private static final Logger LOG = Logger.getLogger(ViewCLI.class.getName());


    private Scanner scanner;

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

            System.out.println("Choose username: ");
            String nickname = scanner.nextLine();

            try {
                connectServer(address, port, type);
                if (!getServer().login(nickname, this)) {
                    System.out.println("Username already used!");
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
    public synchronized List<String> selectObject(List<String> objUuid, int min, int max) {
        System.out.println("Nuova selezione");
        return new ArrayList<>(objUuid.subList(0, min));
    }

    @Override
    public synchronized void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void updateModel(MiniModel model) {
        System.out.println("Received new model");
    }

    @Override
    public synchronized void disconnect() {
        super.disconnect();
        System.out.println("Disconnected from server");
    }

    @Override
    public void ping() {
        System.out.println("Ping ricevuto");
    }
}
