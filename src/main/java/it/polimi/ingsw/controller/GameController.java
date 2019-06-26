package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameController implements Runnable{
    private static final Logger LOG = Logger.getLogger(GameController.class.getName());
    private Match match;
    private TurnController turn;
    private Map<String, RemotePlayer> remotePlayers;
    private Updater updater;

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
    }

    public void spawnRoutine(Player player, int cardsToDraw) throws RemoteException {
        // Dummy PwU, will be removed when the correct message will be implemented
        PowerUp chosenPwu;
        List<PowerUp> tempPowerUps = new ArrayList<>();
        RemotePlayer remotePlayer = remotePlayers.get(player.getNickname());

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
            chosenPwu = remotePlayer.selectIdentifiable(tempPowerUps, 1, 1).get(0);

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
     * This method is what in Toscana they call "Troiaio", has a bunch of try/catch so that all the RemoteExceptions
     * thrown are handled correctly
     */
    public void run() {
        // Send initial model
        updater.updateModelToEveryone();

        // Beginning of the match: everyone should spawn ad get a turn
        if(match.isActive()){
            // While it's the first turn for the current player
            while(match.getCurrentTurn().getType() == TurnType.FIRST_TURN){
                try{
                    // Should check if the player is active
                    if(match.getCurrentTurn().getCurrentPlayer().isActive()){
                        remotePlayers.get(match.getCurrentTurn().getCurrentPlayer().getNickname()).setTimeLeft(20000);
                        spawnRoutine(match.getCurrentTurn().getCurrentPlayer(), 2);
                        turn = new TurnController(match, remotePlayers, updater);
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
        // This is the main game cycle: runs until there are enough players. The game also ends when there have been
        // enough kills
        while(match.isActive() && !match.gameEnded()){
            Player currentPlayer = match.getCurrentTurn().getCurrentPlayer();
            if(currentPlayer.isActive()){
                turn = new TurnController(match, remotePlayers, updater);
                try{
                    // If the player has no square, it means it should respawn
                    if(currentPlayer.getSquare() == null)
                        spawnRoutine(currentPlayer, 1);
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
    }

    private void handleDisconnection(RemotePlayer player) {
        // TODO handle disconnection
        match.getPlayerByNickname(player.getNickname()).setActive(false);
    }

    public Match getMatch(){
        return match;
    }

    public RemotePlayer getRemotePlayer(String nickname){
        return remotePlayers.get(nickname);
    }
}
