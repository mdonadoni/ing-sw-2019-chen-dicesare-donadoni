package it.polimi.ingsw.controller;

import it.polimi.ingsw.common.ServerConfig;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * The Lobby of a match, game starts and lobby is cleared upon countdown ending or maximum number of player reached
 */
public class Lobby {
    /**
     * Logger
     */
    private static final Logger LOG = Logger.getLogger(Lobby.class.getName());
    /**
     * Countdown before starting a match not full.
     */
    private static final long COUNTDOWN = ServerConfig.getLobbyTimeout() * 1000L;
    private static final int MIN_PLAYERS = ServerConfig.getMinPlayers();
    private static final int MAX_PLAYERS = ServerConfig.getMaxPlayers();

    /**
     * Server.
     */
    private ServerController server;
    /**
     * Queue of remote players.
     */
    private ArrayList<RemotePlayer> queue = new ArrayList<>();
    /**
     * True if the start of the match is scheduled, otherwise false.
     */
    private boolean scheduled = false;
    /**
     * Timer to handle countdown and cleaning
     */
    private Timer timer= new Timer();

    /**
     * Constructor of Lobby.
     * @param server Reference to the server.
     */
    public Lobby(ServerController server) {
        this.server = server;
    }

    /**
     * Add player to Lobby.
     * @param player Player to be added.
     */
    public synchronized void addPlayer(RemotePlayer player) {
        LOG.info(() -> "Adding player to lobby: " + player.getNickname());

        // Notify other players
        for (int i = 0; i < queue.size(); i++) {
            RemotePlayer other = queue.get(i);
            other.safeShowMessage("Nuovo utente in lobby: " + player.getNickname());
        }

        queue.add(player);
        if (queue.size() == MAX_PLAYERS) {
            // Match can be started
            startMatch();
        } else if (queue.size() >= MIN_PLAYERS && !scheduled) {
            // Schedule countdown before starting match
            scheduled = true;
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    synchronized (Lobby.this) {
                        if (scheduled) {
                            startMatch();
                        }
                        scheduled = false;
                    }
                }
            };
            timer.schedule(task, COUNTDOWN);
        }
    }

    /**
     * Remove player from Lobby.
     * @param player
     */
    public synchronized void removePlayer(RemotePlayer player) {
        LOG.info(() -> "Removed player from lobby: " + player.getNickname());
        queue.remove(player);
        //TODO fix showMessage
        // Notify other players
        for (int i = 0; i < queue.size(); i++) {
            RemotePlayer other = queue.get(i);
            other.safeShowMessage("Utente rimosso dalla lobby: " + player.getNickname());
        }

        if (queue.size() < MIN_PLAYERS) {
            // Disable scheduling because there are not enough players
            scheduled = false;
        }
    }

    /**
     * Invoke the start of a new match on the server.
     */
    private synchronized void startMatch() {
        // If the there is a scheduled countdown disable it
        scheduled = false;
        ArrayList<RemotePlayer> players = new ArrayList<>(queue);
        queue.clear();
        server.startNewMatch(players);
    }

    public synchronized boolean hasPlayer(RemotePlayer player) {
        return queue.contains(player);
    }
}