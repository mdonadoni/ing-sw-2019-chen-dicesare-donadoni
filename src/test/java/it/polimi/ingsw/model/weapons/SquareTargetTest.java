package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SquareTargetTest {
    Square[][] map;

    // I know I know I should've used an array of players but I didn't want to fuck my mind up with numbers, it's
    // easier to manipulate testcases when players are identified with letters and cells with numbers.
    Player playerA = new Player("Alice", PlayerToken.YELLOW);
    Player playerB = new Player("Bob", PlayerToken.BLUE);
    Player playerC = new Player("Charlie", PlayerToken.GREEN);
    Player playerD = new Player("Dean", PlayerToken.GREY);
    Player playerE = new Player("Eva", PlayerToken.PURPLE);
    Player playerF = new Player("Frank", PlayerToken.YELLOW);
    Player playerG = new Player("Gary", PlayerToken.BLUE);

    @BeforeEach
    void setUpBoardAndPlayers(){
        // Board
        BoardSample bs = new BoardSample();
        map = bs.map;

        // Setup players
        map[1][1].addPlayer(playerA);
        map[3][2].addPlayer(playerB);
        map[3][1].addPlayer(playerC);
        map[2][1].addPlayer(playerD);
        map[2][0].addPlayer(playerE);
        map[2][0].addPlayer(playerF);
        map[0][1].addPlayer(playerG);
        playerA.setSquare(map[1][1]);
        playerB.setSquare(map[3][2]);
        playerC.setSquare(map[3][1]);
        playerD.setSquare(map[2][1]);
        playerE.setSquare(map[2][0]);
        playerF.setSquare(map[2][0]);
        playerG.setSquare(map[0][1]);

    }

    @Test
    void validateTargetPlayer() {
        SquareTarget basic = new SquareTarget(1, Visibility.VISIBLE, -1, 2, false, false, 0, 1, false);
        SquareTarget aBitMoreFar = new SquareTarget(1, Visibility.DC, 1, -1, false, false, 1, 1, false);

        assertTrue(basic.validateTargetPlayer(playerA, map[0][1], playerG));
        assertTrue(aBitMoreFar.validateTargetPlayer(playerF, map[2][1], playerC));
        assertFalse(aBitMoreFar.validateTargetPlayer(playerA, map[1][2], playerG));
        assertFalse(basic.validateTargetPlayer(playerC, map[2][2], playerB));
    }

    @Test
    void compatibleTargetPlayers() {
        SquareTarget onTheSpot = new SquareTarget(2, Visibility.DC, -1, -1, false, false, 0, 3, false);
        SquareTarget aBitMoreFar = new SquareTarget(3, Visibility.DC, -1, -1, false, false, 1, 3, false);
        List<Player> playerList = new ArrayList<>();
        playerList.add(playerC);
        playerList.add(playerB);
        List<Player> anotherPlayerList = new ArrayList<>();
        anotherPlayerList.add(playerA);
        anotherPlayerList.add(playerB);
        anotherPlayerList.add(playerF);
        List<Square> someSquares = new ArrayList<>();
        someSquares.add(map[3][1]);
        someSquares.add(map[3][2]);
        List<Square> otherSquares = new ArrayList<>();
        otherSquares.add(map[1][0]);
        otherSquares.add(map[2][2]);
        otherSquares.add(map[2][0]);

        assertTrue(onTheSpot.compatibleTargetPlayers(playerD, someSquares, playerList));
        assertTrue(aBitMoreFar.compatibleTargetPlayers(playerB, otherSquares, anotherPlayerList));
        assertFalse(onTheSpot.compatibleTargetPlayers(playerG, someSquares, anotherPlayerList));
        assertFalse(aBitMoreFar.compatibleTargetPlayers(playerG, otherSquares, playerList));
    }
}