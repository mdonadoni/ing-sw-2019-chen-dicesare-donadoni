package it.polimi.ingsw.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class Lobby {
    /**
     * Logger
     */
    private static final Logger LOG = Logger.getLogger(Lobby.class.getName());
    /**
     * Countdown before starting a match not full.
     */
    private static final long COUNTDOWN = 30000;
    /**
     * Period to clean lobby of disconnected players
     */
    private static final long CLEAN_PERIOD = 3000;

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
        TimerTask cleanQueue = new TimerTask() {
            @Override
            public void run() {
                synchronized (Lobby.this) {
                    List<RemotePlayer> players = new ArrayList<>(queue);
                    for (RemotePlayer p : players) {
                        if (!p.isConnected()) {
                            removePlayer(p);
                        }
                    }
                }
            }
        };
        timer.schedule(cleanQueue, 0, CLEAN_PERIOD);
    }

    /**
     * Add player to Lobby.
     * @param player Player to be added.
     */
    public synchronized void addPlayer(RemotePlayer player) {
        LOG.info(() -> "Added player to lobby: " + player.getNickname());
        queue.add(player);
        //TODO remove fixed value
        if (queue.size() == 5) {
            // Match can be started
            startMatch();
        } else if (queue.size() >= 3 && !scheduled) {
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
    private synchronized void removePlayer(RemotePlayer player) {
        LOG.info(() -> "Removed player from lobby: " + player.getNickname());
        //TODO signal to all players that this player is disconnected
        queue.remove(player);
        //TODO remove fixed value
        if (queue.size() < 3) {
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
}