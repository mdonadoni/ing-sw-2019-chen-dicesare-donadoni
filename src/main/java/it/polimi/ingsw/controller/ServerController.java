package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.BoardType;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.minified.MiniModel;
import it.polimi.ingsw.network.LocalServer;
import it.polimi.ingsw.network.View;

import java.util.Arrays;
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
        executor.execute(() -> {
            try {
                Match match = new Match(Arrays.asList("A", "B", "C"), BoardType.SMALL);
                Player p = match.getPlayerByNickname("A");
                view.updateModel(new MiniModel(match, p));
            } catch (Exception e) {
                LOG.log(Level.SEVERE, e, () -> "Error while using view " + nickname);
            }
        });
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