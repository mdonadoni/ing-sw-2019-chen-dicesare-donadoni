package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.TestView;
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
        playerMap.put("B", new TestView());
        playerMap.put("C", new TestView());
        GameController controller = new GameController(playerMap, BoardType.SMALL);


        controller.spawnRoutine(controller.getMatch().getPlayerByNickname("Ada"), 4);
        PowerUp usedCard = controller.getMatch().getGameBoard().getPowerUpDeck().getDiscarded().get(0);
        assertEquals(controller.getMatch().getGameBoard().getBoard().getSpawnPointByColor(usedCard.getAmmo()),
                controller.getMatch().getPlayerByNickname("Ada").getSquare());
        for (PowerUp pwu : controller.getMatch().getGameBoard().getPowerUpDeck().getAvaible()){
            assertFalse(controller.getMatch().getPlayerByNickname("Ada").getPowerUps().contains(pwu));
        }
        assertFalse(controller.getMatch().getPlayerByNickname("Ada").getPowerUps().contains(usedCard));
        for(PowerUp pwu : controller.getMatch().getPlayerByNickname("Ada").getPowerUps()){
            assertFalse(controller.getMatch().getGameBoard().getPowerUpDeck().getAvaible().contains(pwu));
            assertFalse(controller.getMatch().getGameBoard().getPowerUpDeck().getDiscarded().contains(pwu));
        }
    }
}