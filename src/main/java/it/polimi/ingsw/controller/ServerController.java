package it.polimi.ingsw.controller;

import it.polimi.ingsw.common.ServerConfig;
import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.model.BoardType;
import it.polimi.ingsw.network.LocalServer;
import it.polimi.ingsw.network.View;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerController extends LocalServer {
    private static final Logger LOG = Logger.getLogger(ServerController.class.getName());

    private Map<String, RemotePlayer> connectedUser = new HashMap<>();
    private Map<String, GameController> nicknameToGame = new HashMap<>();
    private Executor executor = Executors.newCachedThreadPool();
    private Lobby lobby = new Lobby(this);
    private Random rand = new Random();

    @Override
    public synchronized boolean login(String nickname, View view) {
        if (connectedUser.containsKey(nickname)) {
            LOG.log(Level.INFO, "Username already used: {0}", nickname);
            return false;
        }

        if(nickname.length() < 3 || nickname.length() > 9){
            LOG.log(Level.INFO, "Nickname too long or too short: {0}", nickname);
            return false;
        }

        LOG.log(Level.INFO, "New user connected: {0}", nickname);
        RemotePlayer player = new RemotePlayer(nickname, view);
        player.setDisconnectionCallback(this::handleDisconnection);
        connectedUser.put(nickname, player);

        GameController game = nicknameToGame.get(player.getNickname());
        if (game == null || game.isFinished()) {
            lobby.addPlayer(player);
        } else {
            game.addReconnectingPlayer(player);
        }
        return true;
    }

    /**
     * Start a new match with given list of players.
     * @param players List of remote players.
     */
    public synchronized void startNewMatch(List<RemotePlayer> players) {
        LOG.log(Level.INFO, "New match starting");
        //TODO select board
        GameController game = new GameController(players, selectBoard(players.size()));
        players.forEach(remotePlayer -> {
            nicknameToGame.put(remotePlayer.getNickname(), game);
        });
        executor.execute(game);
    }

    public synchronized void handleDisconnection(RemotePlayer player) {
        LOG.log(Level.INFO, "Removing {0} from connected users", player.getNickname());
        connectedUser.remove(player.getNickname());
        if (lobby.hasPlayer(player)) {
            lobby.removePlayer(player);
        }
        //TODO remove from game
    }

    private BoardType selectBoard(int numberOfPlayers){
        if(numberOfPlayers == ServerConfig.getMinPlayers())
            return BoardType.SMALL;
        else if(numberOfPlayers == ServerConfig.getMaxPlayers())
            return BoardType.BIG;
        else{
            List<BoardType> boards = Arrays.asList(BoardType.MEDIUM_1, BoardType.MEDIUM_2);
            return boards.get(rand.nextInt(boards.size()));
        }
    }
}