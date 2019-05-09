package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.View;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    @Test
    void spawnRoutine() throws ResourceException, RemoteException {
        Map<String, View> playerMap = new HashMap<>();
        String playerA = "Ada";
        TestView viewA = new TestView();
        playerMap.put(playerA, viewA);
        GameController controller = new GameController(playerMap, BoardType.SMALL);

        controller.spawnRoutine(controller.getMatch().getPlayers().get(0), 4);
        PowerUp usedCard = controller.getMatch().getGameBoard().getPowerUpDeck().getDiscarded().get(0);
        assertEquals(controller.getMatch().getGameBoard().getBoard().getSpawnPointByColor(usedCard.getAmmo()),
                    controller.getMatch().getPlayers().get(0).getSquare());
        for (PowerUp pwu : controller.getMatch().getGameBoard().getPowerUpDeck().getAvaible()){
            assertFalse(controller.getMatch().getPlayers().get(0).getPowerUps().contains(pwu));
        }
        assertFalse(controller.getMatch().getPlayers().get(0).getPowerUps().contains(usedCard));
        for(PowerUp pwu : controller.getMatch().getPlayers().get(0).getPowerUps()){
            assertFalse(controller.getMatch().getGameBoard().getPowerUpDeck().getAvaible().contains(pwu));
            assertFalse(controller.getMatch().getGameBoard().getPowerUpDeck().getDiscarded().contains(pwu));
        }
    }
}