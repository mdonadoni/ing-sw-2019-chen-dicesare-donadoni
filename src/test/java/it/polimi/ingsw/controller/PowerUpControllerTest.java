package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.TestView;
import it.polimi.ingsw.network.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PowerUpControllerTest {
    PowerUpController pwuC;
    Match match;
    Board board;
    PowerUp teleporter = new PowerUp(PowerUpType.TELEPORTER, AmmoColor.RED);
    PowerUp newton = new PowerUp(PowerUpType.NEWTON, AmmoColor.BLUE);
    List<String> nicks;
    Map<String, RemotePlayer> remotePlayers;

    @BeforeEach
    void setup() throws ResourceException {
        nicks = new ArrayList<>();
        nicks.add("Ada");
        nicks.add("Bob");
        nicks.add("Charlie");
        remotePlayers = new HashMap<>();
        for (String nick : nicks)
            remotePlayers.put(nick, new RemotePlayer(nick, new TestView()));
        match = new Match(nicks, BoardType.MEDIUM_2);
        board = match.getGameBoard().getBoard();
        pwuC = new PowerUpController(match, remotePlayers);
    }

    @Test
    void activatePowerUpTeleporter() throws RemoteException {
        // Since the testView default select always selects the first Identifiable in the list...
        Square shouldBeThisOne = match.getGameBoard().getBoard().getAllSquares().get(0);
        Player ada = match.getPlayerByNickname("Ada");

        remotePlayers.get("Ada").setTimeLeft(20000);
        pwuC.activatePowerUp(teleporter, nicks.get(0));

        // Let's see if we messed up the squares
        checkBoard(shouldBeThisOne, ada);
        // Noice, now we should test if Ada is in the correct square
        assertEquals(ada.getSquare(), shouldBeThisOne);
    }

    @Test
    void activatePowerUpNewton() throws RemoteException{
        // Ada is attacking, Bob is the target
        // So, Bob should move in this square, because testView selects the first of the prompted squares
        Player bob = match.getPlayerByNickname("Bob");
        bob.setSquare(board.getSpawnPointByColor(AmmoColor.BLUE));
        Square bobSquare = bob.getSquare();
        Square shouldBeThisOne = bobSquare.getSquaresByDistanceAligned(2).get(0);

        remotePlayers.get("Ada").setTimeLeft(20000);
        pwuC.activatePowerUp(newton, "Ada", bob);

        // Let's see if we messed up the squares
        checkBoard(shouldBeThisOne, bob);
        // Noice, now we should test if Ada is in the correct square
        assertEquals(bob.getSquare(), shouldBeThisOne);

    }

    void checkBoard(Square square, Player player){
        for (Square sq : board.getAllSquares()){
            if (!sq.equals(square))
                assertFalse(sq.getPlayers().contains(player));
            else
                assertTrue(sq.getPlayers().contains(player));
        }
    }
}