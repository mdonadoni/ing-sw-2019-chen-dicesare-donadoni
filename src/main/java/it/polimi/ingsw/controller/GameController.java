package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.View;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameController implements Runnable{
    private static final Logger LOG = Logger.getLogger(GameController.class.getName());
    private Match match;
    private Map<String, View> users;

    public GameController(Match match){
        this.match = match;
    }

    public GameController(Map<String, View> connectedPlayers, BoardType bdType) throws ResourceException {
        match = new Match();
        for(String nick : connectedPlayers.keySet()){
            match.addPlayer(new Player(nick));
        }
        match.getPlayers().get(0).setStartingPlayer(true);
        match.getGameBoard().initBoard(bdType);
        users = connectedPlayers;
    }

    public void spawnRoutine(Player player, int cardsToDraw) throws RemoteException {
        // Dummy PwU, will be removed when the correct message will be implemented
        String chosen;
        Optional<PowerUp> chosenPwu;
        List<PowerUp> tempPowerUps = new ArrayList<>();
        List<String> toSend = new ArrayList<>();

        if(player.getSquare() == null && player.getActive()){
            // Draw PowerUps from the deck
            for (int i = 0; i < cardsToDraw; i++)
                tempPowerUps.add(match.getGameBoard().getPowerUpDeck().draw());

            // Add to the sending list also the power-up currently in the player's inventory
            tempPowerUps.addAll(player.getPowerUps());
            player.getPowerUps().clear();
            // Finally build the uuid list
            for (PowerUp pwu : tempPowerUps){
                toSend.add(pwu.getUuid());
            }

            // Send tempPowerUps and wait for a response
            LOG.log(Level.INFO, "Sending message to {0}", player.getNickname());
            chosen = users.get(player.getNickname()).selectObject(toSend, 1, 1).get(0);
            chosenPwu = tempPowerUps.stream()
                        .filter(e -> e.getUuid().equals(chosen))
                        .findAny();

            if (chosenPwu.isPresent()){
                tempPowerUps.remove(chosenPwu.get());

                // Place the player down on the board
                player.setSquare(match.getGameBoard().getBoard().getSpawnPointByColor(chosenPwu.get().getAmmo()));
                match.getGameBoard().getBoard().getSpawnPointByColor(chosenPwu.get().getAmmo()).addPlayer(player);

                // Add the remaining drawn powerUps (if any) to the player
                player.getPowerUps().addAll(tempPowerUps);

                // Finally discard the used PowerUp
                match.getGameBoard().getPowerUpDeck().discard(chosenPwu.get());
            }
        }
    }

    public void run() {
        // All these things are here to make tests

        for (Player player : match.getPlayers()){
            try {
                spawnRoutine(player, 2);
            } catch (RemoteException e){
                LOG.log(Level.WARNING, "User {0} cannot be reached, setting him inactive...", player.getNickname());
                player.setActive(false);
            }
        }
    }

    public Match getMatch(){
        return match;
    }
}
