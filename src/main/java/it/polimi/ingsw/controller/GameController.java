package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.View;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameController implements Runnable{
    private static final Logger LOG = Logger.getLogger(GameController.class.getName());
    private Match match;
    private Map<String, View> users;
    private TurnController turn;
    private Map<String, RemotePlayer> remotePlayers;

    public GameController(Match match){
        this.match = match;
    }

    public GameController(Map<String, View> connectedPlayers, BoardType bdType) throws ResourceException {
        match = new Match(new ArrayList<>(connectedPlayers.keySet()), bdType);
        users = connectedPlayers;
        remotePlayers = new HashMap<>();
        for(String nick : connectedPlayers.keySet()){
            remotePlayers.put(nick, new RemotePlayer(nick, connectedPlayers.get(nick)));
        }
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
        if(match.isActive()){
            // First turn for everyone!
            while(match.getCurrentTurn().getType() == TurnType.FIRST_TURN){
                try{
                    if(match.getCurrentTurn().getCurrentPlayer().isActive()){
                        spawnRoutine(match.getCurrentTurn().getCurrentPlayer(), 2);
                        turn = new TurnController(match);
                        turn.startTurn();
                    }
                    match.nextTurn();
                } catch(RemoteException e){
                    LOG.log(Level.WARNING, "Player {0} disconnected, setting him inactive...",
                            match.getCurrentTurn().getCurrentPlayer().getNickname());
                    match.getCurrentTurn().getCurrentPlayer().setActive(false);
                }

            }
        }

        while(match.isActive()){
            if(match.getCurrentTurn().getCurrentPlayer().isActive()){
                turn = new TurnController(match);
                turn.startTurn();
                match.nextTurn();
            }
        }
    }

    public Match getMatch(){
        return match;
    }

    public RemotePlayer getRemotePlayer(String nickname){
        return remotePlayers.get(nickname);
    }
}
