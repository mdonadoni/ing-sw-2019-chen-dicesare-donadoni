package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.BoardType;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.PowerUp;
import it.polimi.ingsw.network.TestView;
import it.polimi.ingsw.util.ResourceException;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GameControllerTest {

    @Test
    void spawnRoutine() throws ResourceException, RemoteException {
        List<RemotePlayer> players = new ArrayList<>();
        String playerA = "Ada";
        TestView viewA = new TestView();
        players.add(new RemotePlayer(playerA, viewA));
        players.add(new RemotePlayer("B", new TestView()));
        players.add(new RemotePlayer("C", new TestView()));

        GameController controller = new GameController(players, BoardType.SMALL);
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