package it.polimi.ingsw.view;

import it.polimi.ingsw.common.StandingsItem;
import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.model.minified.MiniModel;
import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.network.LocalView;

import java.text.MessageFormat;
import java.util.*;

/**
 * Test bot simulate a player
 */
public class ViewBot extends LocalView implements Runnable {
    /**
     * Random generator
     */
    private static final Random RAND = new Random();
    /**
     * Model of the bot
     */
    private MiniModel model;
    /**
     * Type of connection
     */
    private ConnectionType connection;
    /**
     * The address of the server
     */
    private String server;
    /**
     * The number of the port
     */
    private int port;

    /**
     * Constructor of the class
     * @param server The address of the server
     * @param port The number of the port
     * @param connection The type of connection
     */
    public ViewBot(String server, int port, ConnectionType connection) {
        this.connection = connection;
        this.server = server;
        this.port = port;
    }

    /**
     * Generate a random int between 2 values
     * @param a The minimum value
     * @param b The maximum value
     * @return A random int between the 2 values
     */
    private static int randInt(int a, int b) {
        int delta = b-a+1;
        int rand = RAND.nextInt(delta);
        return a + rand;
    }

    /**
     * Select a object between a list.
     * @param objUuid List of the UUID of the objects.
     * @param min Minimum of object.
     * @param max Maximum of object.
     * @param dialog The dialog type
     * @return Selected object.
     */
    @Override
    public synchronized ArrayList<String> selectObject(ArrayList<String> objUuid, int min, int max, Dialog dialog) {
        System.out.println("SELECT: " + objUuid + " min " + min + " max " +  max);

        // Check if MiniModel actually has what we need to search for
        for (String uuid : objUuid) {
            String desc = Descriptions.find(model, uuid);
            if (desc == null) {
                System.out.println("ERROR " + uuid);
                System.exit(1);
            }

            System.out.println(uuid + " " + desc);
        }

        ArrayList<String> shuffled = new ArrayList<>(objUuid);
        Collections.shuffle(shuffled, RAND);

        if (min > max || min < 0 || max > objUuid.size()) {
            System.out.println("ERROR wrong min or max");
            System.exit(2);
        }

        ArrayList<String> res = new ArrayList<>(shuffled.subList(0, randInt(min, max)));
        System.out.println("SELEZIONATO: " + res);
        return res;
    }

    /**
     * Show a message
     * @param message Massage to be shown.
     */
    @Override
    public synchronized void showMessage(String message) {
        System.out.println("MESSAGE: " + message);
    }

    /**
     * Update the model
     * @param model Updated model.
     */
    @Override
    public synchronized void updateModel(MiniModel model) {
        System.out.println("NEW MODEL");
        this.model = model;
    }

    /**
     * Notify the end of the match.
     * @param standings Final Standings of the game.
     */
    @Override
    public synchronized void notifyEndMatch(ArrayList<StandingsItem> standings) {
        System.out.println("FINE MATCH");
        for (StandingsItem s : standings) {
            System.out.println(
                    MessageFormat.format(
                            "{0}) {1} ({2} punti)",
                            s.getPosition(),
                            s.getNickname(),
                            s.getPoints()));
        }
    }

    /**
     * Run the bot.
     */
    @Override
    public void run() {
        try {
            connectServer(server, port, connection);
            String name = UUID.randomUUID().toString().substring(0, 5);
            System.out.println("IO SONO " + name);
            getServer().login(name, this);
        } catch (Exception e) {
            e.printStackTrace();
            closeConnection();
        }
    }
}
