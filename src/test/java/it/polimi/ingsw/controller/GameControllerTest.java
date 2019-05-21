package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.TestView;
import it.polimi.ingsw.network.View;
import it.polimi.ingsw.util.ResourceException;
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
        Match match = controller.getMatch();
        GameBoard gameBoard = match.getGameBoard();

        controller.getRemotePlayer("Ada").setTimeLeft(20000);
        controller.spawnRoutine(match.getPlayerByNickname("Ada"), 4);

        PowerUp usedCard = gameBoard.getPowerUpDeck().getDiscarded().get(0);
        assertEquals(gameBoard.getBoard().getSpawnPointByColor(usedCard.getAmmo()),
                match.getPlayerByNickname("Ada").getSquare());

        for (PowerUp pwu : gameBoard.getPowerUpDeck().getAvaible()){
            assertFalse(match.getPlayerByNickname("Ada").getPowerUps().contains(pwu));
        }
        assertFalse(match.getPlayerByNickname("Ada").getPowerUps().contains(usedCard));

        for(PowerUp pwu : match.getPlayerByNickname("Ada").getPowerUps()){
            assertFalse(gameBoard.getPowerUpDeck().getAvaible().contains(pwu));
            assertFalse(gameBoard.getPowerUpDeck().getDiscarded().contains(pwu));
        }
    }
}