package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.LocalServer;
import it.polimi.ingsw.network.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerController extends LocalServer {
    private static final Logger LOG = Logger.getLogger(ServerController.class.getName());

    private Map<String, RemotePlayer> connectedUser = new HashMap<>();
    private Executor executor = Executors.newCachedThreadPool();
    private Lobby lobby = new Lobby(this);

    @Override
    public synchronized boolean login(String nickname, View view) {
        if (connectedUser.containsKey(nickname)) {
            LOG.log(Level.WARNING, "Username already used: {0}", nickname);
            return false;
        }
        RemotePlayer player = new RemotePlayer(nickname, view);
        connectedUser.put(nickname, player);
        lobby.addPlayer(player);
        /*executor.execute(() -> {
            try {
                view.showMessage(MessageFormat.format("Ciao {0}! Io sono il server!", nickname));
                view.showMessage("Fra cinque secondi sarai disconnesso!");
                for (int i = 5; i > 0; i--) {
                    view.showMessage(String.valueOf(i));
                    Thread.sleep(1000);
                }
                view.showMessage("Ora ti disconnetto!");
                view.disconnect();
                LOG.info(() -> "User disconnected: " + nickname);
            } catch (Exception e) {
                LOG.log(Level.SEVERE, e, () -> "Error disconnecting " + nickname);
            }
        });*/
        LOG.log(Level.INFO, "New user connected: {0}", nickname);
        return true;
    }

    /**
     * Start a new match with given list of players.
     * @param players List of remote players.
     */
    public synchronized void startNewMatch(List<RemotePlayer> players) {
        //TODO start new match
        LOG.log(Level.INFO, "New match starting");
    }


}