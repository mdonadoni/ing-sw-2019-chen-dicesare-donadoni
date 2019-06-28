package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameController implements Runnable{
    private static final Logger LOG = Logger.getLogger(GameController.class.getName());
    private Match match;
    private TurnController turn;
    private Map<String, RemotePlayer> remotePlayers;
    private Updater updater;
    private AtomicBoolean finished = new AtomicBoolean(false);
    private ScoreController scoreController;

    public GameController(Match match){
        this.match = match;
    }

    public GameController(List<RemotePlayer> connectedPlayers, BoardType bdType) {
        List<String> nicknames = new ArrayList<>();
        connectedPlayers.forEach(remotePlayer -> nicknames.add(remotePlayer.getNickname()));
        match = new Match(nicknames, new JsonModelFactory(bdType));
        remotePlayers = new HashMap<>();
        connectedPlayers.forEach(remotePlayer -> remotePlayers.put(remotePlayer.getNickname(), remotePlayer));
        updater = new Updater(remotePlayers, match);
        turn = new TurnController(match, remotePlayers, updater);
        scoreController = new ScoreController(match);
    }

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
            chosenPwu = remotePlayer.selectIdentifiable(tempPowerUps, 1, 1, SelectDialog.SPAWN_DIALOG).get(0);

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
     * thrown are handled correctly
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
        if(match.isActive()){
            // While it's the first turn for the current player
            while(match.getCurrentTurn().getType() == TurnType.FIRST_TURN){
                Player currentPlayer = match.getCurrentTurn().getCurrentPlayer();
                try{
                    // Should check if the player is active
                    if(currentPlayer.isActive()){
                        spawnRoutine(currentPlayer, 2);
                        turn.startTurn();
                    }
                    match.nextTurn();
                } catch(RemoteException e){
                    LOG.log(Level.WARNING, "Player {0} disconnected, setting him inactive...",
                            match.getCurrentTurn().getCurrentPlayer().getNickname());
                    handleDisconnection(remotePlayers.get(match.getCurrentTurn().getCurrentPlayer().getNickname()));
                }
            }
        }

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
                    LOG.log(Level.WARNING, "Player {0} disconnected, setting him inactive...",
                            currentPlayer.getNickname());
                    handleDisconnection(remotePlayers.get(currentPlayer.getNickname()));
                }
            }
            match.nextTurn();
        }

        // The match is finished
        LOG.log(Level.INFO, "Match {0} finished", match.getUuid());
        LOG.log(Level.INFO, "Calculating final scores");
        scoreController.endGamePoints();
        finished.set(true);
    }

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
        // TODO handle disconnection
        match.getPlayerByNickname(player.getNickname()).setActive(false);
    }

    Match getMatch(){
        return match;
    }

    RemotePlayer getRemotePlayer(String nickname){
        return remotePlayers.get(nickname);
    }
}
