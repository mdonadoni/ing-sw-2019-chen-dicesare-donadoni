package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.minified.MiniModel;

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

    public GameController(Match match){
        this.match = match;
    }

    public GameController(List<RemotePlayer> connectedPlayers, BoardType bdType) {
        List<String> nicknames = new ArrayList<>();
        connectedPlayers.forEach(remotePlayer -> nicknames.add(remotePlayer.getNickname()));
        match = new Match(nicknames, new JsonModelFactory(bdType));
        remotePlayers = new HashMap<>();
        connectedPlayers.forEach(remotePlayer -> remotePlayers.put(remotePlayer.getNickname(), remotePlayer));
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
        }
    }
    // Don't take this method too seriously, it's just an idea on how it should work
    public void run() {
        // Send initial model
        remotePlayers.forEach((nickname, player) -> {
            try {
                player.updateModel(
                        new MiniModel(match, match.getPlayerByNickname(nickname)),
                        5000
                );
            } catch (RemoteException e) {
                handleDisconnection(player);
            }
        });

        // Do things
        if(match.isActive()){
            // First turn for everyone!
            while(match.getCurrentTurn().getType() == TurnType.FIRST_TURN){
                try{
                    if(match.getCurrentTurn().getCurrentPlayer().isActive()){
                        spawnRoutine(match.getCurrentTurn().getCurrentPlayer(), 2);
                        turn = new TurnController(match, remotePlayers);
                        turn.startTurn();
                    }
                    match.nextTurn();
                } catch(RemoteException e){
                    LOG.log(Level.WARNING, "Player {0} disconnected, setting him inactive...",
                            match.getCurrentTurn().getCurrentPlayer().getNickname());
                    handleDisconnection(
                            remotePlayers.get(
                                match.getCurrentTurn().getCurrentPlayer().getNickname()
                            )
                    );
                }

            }
        }

        while(match.isActive()){
            if(match.getCurrentTurn().getCurrentPlayer().isActive()){
                turn = new TurnController(match, remotePlayers);
                turn.startTurn();
                match.nextTurn();
            }
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
