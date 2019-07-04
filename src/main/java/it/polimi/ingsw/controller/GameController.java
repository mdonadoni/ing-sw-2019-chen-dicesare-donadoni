package it.polimi.ingsw.controller;

import it.polimi.ingsw.common.StandingsItem;
import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.model.*;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main controller class that manages the match.
 */
public class GameController implements Runnable{
    /**
     * Logger to be used by this class.
     */
    private static final Logger LOG = Logger.getLogger(GameController.class.getName());
    /**
     * Message used to log disconnections.
     */
    private static final String PLAYER_DISCONNECTED_LOG = "Player {0} disconnected, setting him inactive...";
    /**
     * The match that is going on
     */
    private Match match;
    /**
     * Controller that handles a single turn
     */
    private TurnController turn;
    /**
     * A map that matches a remotePlayer with the player nickname
     */
    private Map<String, RemotePlayer> remotePlayers;
    /**
     * Contains method used to send updates to the players
     */
    private Updater updater;
    /**
     * Boolean value that states whether the match has finished
     */
    private AtomicBoolean finished = new AtomicBoolean(false);
    /**
     * Controller that calculates the score and handles players' death
     */
    private ScoreController scoreController;
    /**
     * When someone reconnects to the game, it is added in this waiting list
     */
    private final List<RemotePlayer> waitingList;
    /**
     * Contains method used to send all the players some messages
     */
    private Notifier notifier;

    /**
     * Constructor of the GameController, initialises everything is needed
     * @param connectedPlayers The map with all the remotePlayers
     * @param bdType The type of the board used in this match
     */
    public GameController(List<RemotePlayer> connectedPlayers, BoardType bdType) {
        List<String> nicknames = new ArrayList<>();
        connectedPlayers.forEach(remotePlayer -> nicknames.add(remotePlayer.getNickname()));
        match = new Match(nicknames, new JsonModelFactory(bdType));
        remotePlayers = new HashMap<>();
        connectedPlayers.forEach(remotePlayer -> remotePlayers.put(remotePlayer.getNickname(), remotePlayer));
        updater = new Updater(remotePlayers, match);
        turn = new TurnController(match, remotePlayers, updater);
        scoreController = new ScoreController(match);
        waitingList = new ArrayList<>();
        notifier = new Notifier(remotePlayers, match);
    }

    /**
     * This method allows a player to spawn on the board correctly, according to the rules of the game
     * @param player The player that needs to spawn
     * @param cardsToDraw How many powerups he can draw to spawn
     * @throws RemoteException In case something goes wrong
     */
    public void spawnRoutine(Player player, int cardsToDraw) throws RemoteException {
        // Dummy PwU, will be removed when the correct message will be implemented
        PowerUp chosenPwu;
        List<PowerUp> tempPowerUps = new ArrayList<>();
        RemotePlayer remotePlayer = remotePlayers.get(player.getNickname());

        remotePlayer.setTimeLeft(60000);

        LOG.log(Level.INFO, "Starting a spawn routine for {0}", player.getNickname());

        if(player.getSquare() == null && player.isActive()){
            // Draw PowerUps from the deck
            for (int i = 0; i < cardsToDraw; i++)
                player.addDrawnPowerUp(match.getGameBoard().getPowerUpDeck().draw());

            updater.updateModel(player.getNickname());

            // Add to the sending list also the power-up currently in the player's inventory
            tempPowerUps.addAll(player.getPowerUps());
            tempPowerUps.addAll(player.getDrawnPowerUps());

            // Send tempPowerUps and wait for a response
            LOG.log(Level.INFO, "Sending message to {0}", player.getNickname());
            chosenPwu = remotePlayer.selectIdentifiable(tempPowerUps, 1, 1, Dialog.SPAWN).get(0);

            player.removeDrawnPowerUp(chosenPwu);
            player.discardPowerUp(chosenPwu);

            // Place the player down on the board
            player.setSquare(match.getGameBoard().getBoard().getSpawnPointByColor(chosenPwu.getAmmo()));
            match.getGameBoard().getBoard().getSpawnPointByColor(chosenPwu.getAmmo()).addPlayer(player);

            // Add the remaining drawn powerUps (if any) to the player
            for (PowerUp pwu : player.getDrawnPowerUps())
                player.addPowerUp(pwu);
            player.clearDrawnPowerUps();

            // Finally discard the used PowerUp
            match.getGameBoard().getPowerUpDeck().discard(chosenPwu);

            updater.updateModel(player.getNickname());
        }
    }

    /**
     * Checks whether someone needs to resapawn and eventually start the spawn routine
     */
    private void checkForPeopleToRespawn(){
        LOG.log(Level.INFO, "Checking if someone has to respawn");
        for(Player player : match.getPlayers()){
            if(player.getSquare() == null && player.isActive()){
                try{
                    spawnRoutine(player, 1);
                }catch(RemoteException e){
                    player.setActive(false);
                }
            }
        }
    }

    /**
     * This method is what in Toscana they call "Troiaio", has a bunch of try/catch so that all the RemoteExceptions
     * thrown are handled correctly. Also runs the match, initialising it at the beginning, running standard turns
     * and then triggers the Final Frenzy.
     */
    public void run() {
        LOG.log(Level.INFO, "Starting match {0}", match.getUuid());
        LOG.log(Level.INFO, "Giving everyone some ammos");
        for(Player player : match.getPlayers()){
            player.addAmmo(AmmoColor.RED);
            player.addAmmo(AmmoColor.BLUE);
            player.addAmmo(AmmoColor.YELLOW);
        }

        // Send initial model
        LOG.log(Level.INFO, "Sending initial model to everyone");
        updater.updateModelToEveryone();

        LOG.log(Level.INFO, "First turn for everyone");
        // Beginning of the match: everyone should spawn ad get a turn
        // While it's the first turn for the current player
        while(match.isActive() && match.getCurrentTurn().getType() == TurnType.FIRST_TURN){
            Player currentPlayer = match.getCurrentTurn().getCurrentPlayer();
            try{
                spawnRoutine(currentPlayer, 2);
                turn.startTurn();
            } catch(RemoteException e){
                LOG.log(Level.WARNING, PLAYER_DISCONNECTED_LOG,
                        match.getCurrentTurn().getCurrentPlayer().getNickname());
                handleDisconnection(remotePlayers.get(match.getCurrentTurn().getCurrentPlayer().getNickname()));
            }
            match.nextTurn();
        }

        addReconnectedPlayers();

        LOG.log(Level.INFO, "Main game cycle starting now...");
        // This is the main game cycle: runs until there are enough players. The game also ends when there have been
        // enough kills
        while(match.isActive() && !match.gameEnded()){
            Player currentPlayer = match.getCurrentTurn().getCurrentPlayer();
            // When someone dies, he should respawn
            checkForPeopleToRespawn();
            if(currentPlayer.isActive()){
                try{
                    // Finally start his turn
                    turn.startTurn();
                }catch(RemoteException e){
                    LOG.log(Level.WARNING, PLAYER_DISCONNECTED_LOG,
                            currentPlayer.getNickname());
                    handleDisconnection(remotePlayers.get(currentPlayer.getNickname()));
                }
            }
            if(match.gameEnded())
                match.activateFinalFrenzy();
            addReconnectedPlayers();
            match.nextTurn();
        }

        // Now it's time for final frenzy, which has already been triggered
        if(match.isActive()){
            LOG.log(Level.INFO, "Final Frenzy has started");
            notifier.notifyEveryone(Dialog.FINAL_FRENZY_STARTED);
        }

        int numberOfActivePlayers = (int) match.getPlayers().stream()
                .filter(Player::isActive)
                .count();
        for(int i=0; i<numberOfActivePlayers && match.isActive(); i++){
            Player currentPlayer = match.getCurrentTurn().getCurrentPlayer();
            checkForPeopleToRespawn();
            try{
                turn.startTurn();
            } catch(RemoteException e){
                LOG.log(Level.WARNING, PLAYER_DISCONNECTED_LOG,
                        currentPlayer.getNickname());
                handleDisconnection(remotePlayers.get(currentPlayer.getNickname()));
            }
            addReconnectedPlayers();
            match.nextTurn();
        }

        // The match is finished
        LOG.log(Level.INFO, "Match {0} finished", match.getUuid());
        finished.set(true);
        LOG.log(Level.INFO, "Calculating final scores");
        scoreController.endGamePoints();
        notifyEndMatch();
    }

    /**
     * This method is for notifying everyone that the match is over and then sends the final standings
     */
    private void notifyEndMatch() {
        List<StandingsItem> standings = scoreController.getFinalStandings();
        for (RemotePlayer p : remotePlayers.values()) {
            try {
                // TODO hardcoded value
                p.notifyEndMatch(standings, 5000);
                p.disconnect();
            } catch (RemoteException e) {
                LOG.warning(() -> "Couldn't send final standings to " + p.getNickname());
            }
        }

    }

    /**
     * @return Whether the match has finished
     */
    public boolean isFinished() {
        return finished.get();
    }

    /**
     * When a player disconnects, if the controller tries to ask him something, a RemoteException is thrown. The exception
     * runs up till this level where it's handled. Basically this method fixes the model so that when a player disconnects
     * the model remains in a coherent state.
     * @param player The player who disconnected
     */
    private void handleDisconnection(RemotePlayer player) {
        match.getPlayerByNickname(player.getNickname()).setActive(false);
        notifier.notifyEveryone(Dialog.PLAYER_DISCONNECTED, player.getNickname());
    }

    /**
     * @return The match going on
     */
    Match getMatch(){
        return match;
    }

    /**
     * @param nickname The nickname of the player
     * @return A RemoteView of the prompted player
     */
    RemotePlayer getRemotePlayer(String nickname){
        return remotePlayers.get(nickname);
    }

    /**
     * Adds a Player to the waiting list
     * @param reconnectingPlayer The player you want to add
     */
    public synchronized void addReconnectingPlayer(RemotePlayer reconnectingPlayer){
        waitingList.add(reconnectingPlayer);
    }

    /**
     * Puts back in the game all the disconnected players that are now waiting to be reconnected
     */
    private synchronized void addReconnectedPlayers(){
        for(RemotePlayer remotePlayer : waitingList){
            remotePlayers.replace(remotePlayer.getNickname(), remotePlayer);
            match.getPlayerByNickname(remotePlayer.getNickname()).setActive(true);
            fixPlayerModel(remotePlayer.getNickname());
            updater.updateModelToEveryone();

            notifier.notifyEveryone(Dialog.PLAYER_RECONNECTED, remotePlayer.getNickname());
        }

        waitingList.clear();
    }

    /**
     * When someone disconnects it may have something wrong with his model, this method fixes everything upon his reconnection
     * @param nickname The Player who is reconnecting
     */
    private void fixPlayerModel(String nickname) {
        Player player = match.getPlayerByNickname(nickname);

        for(PowerUp pwu : player.getDrawnPowerUps())
            match.getGameBoard().getPowerUpDeck().discard(pwu);

        player.clearDrawnPowerUps();
    }
}