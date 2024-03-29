package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TargetTest {
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
    void constructorTest(){
        assertThrows(InvalidParameterException.class, () -> {
            new Target(1, Visibility.INVISIBLE, -3, -2, false, false);});
        assertThrows(InvalidParameterException.class, () -> {
            new Target(0, Visibility.INVISIBLE, 3, 2, false, false);});
        assertThrows(InvalidParameterException.class, () -> {
            new Target(1, Visibility.INVISIBLE, 1, -2, false, false);});
        assertThrows(InvalidParameterException.class, () -> {
            new Target(1, Visibility.INVISIBLE, 3, 0, false, false);});
    }

    @Test
    void validateTargetSquare() {
        // Setup targets
        Target targetVis = new Target(1, Visibility.VISIBLE, 1, -1, false, false);
        Target targetInv = new Target(1, Visibility.INVISIBLE, -1, 3, false, false);
        Target longShot = new Target(1, Visibility.VISIBLE, 2, -1, false, false);
        longShot.addSpecial(SpecialArea.LINE);
        Target closeCombat = new Target(1, Visibility.DC, -1, 0, false, false);

        // Let's see what happens...
        assertTrue(targetVis.validateTargetSquare(playerA, playerB.getSquare()));
        assertFalse(targetVis.validateTargetSquare(playerC, playerA.getSquare()));
        assertTrue(targetInv.validateTargetSquare(playerB, playerA.getSquare()));
        assertFalse(targetInv.validateTargetSquare(playerB, playerC.getSquare()));
        assertTrue(longShot.validateTargetSquare(playerD, playerG.getSquare()));
        assertFalse(longShot.validateTargetSquare(playerB, playerA.getSquare()));
        assertFalse(longShot.validateTargetSquare(playerF, playerE.getSquare()));
        assertTrue(closeCombat.validateTargetSquare(playerE, playerF.getSquare()));
        assertFalse(closeCombat.validateTargetSquare(playerA, playerG.getSquare()));


    }

    @Test
    void compatibleTargetSquares() {
        // Setup targets
        Target targetLine = new Target(2, Visibility.DC, -1, 2, false, false);
        targetLine.addSpecial(SpecialArea.LINE);
        List<Square> targetLineList1 = new ArrayList<>();
        List<Square> targetLineList2 = new ArrayList<>();
        List<Square> targetLineList3 = new ArrayList<>();
        targetLineList1.add(playerD.getSquare());
        targetLineList1.add(playerA.getSquare());
        targetLineList2.add(playerA.getSquare());
        targetLineList2.add(playerC.getSquare());
        targetLineList3.add(playerA.getSquare());

        Target machineGun = new Target(3, Visibility.VISIBLE, -1, 3, false, false);
        List<Square> machineGunList1 = new ArrayList<>();
        machineGunList1.add(playerA.getSquare());
        machineGunList1.add(playerB.getSquare());
        machineGunList1.add(playerG.getSquare());

        Target twoShots = new Target(2, Visibility.INVISIBLE, -1, 3, false, false);
        List<Square> machineGunList2 = new ArrayList<>();
        machineGunList2.add(playerE.getSquare());
        machineGunList2.add(playerF.getSquare());

        List<Square> machineGunList3 = new ArrayList<>();
        machineGunList3.add(playerB.getSquare());
        machineGunList3.add(playerD.getSquare());

        // Dear Lord, make these tests pass
        assertTrue(targetLine.compatibleTargetSquares(playerC, targetLineList1));
        assertFalse(targetLine.compatibleTargetSquares(playerD, targetLineList2));
        assertTrue(targetLine.compatibleTargetSquares(playerC, targetLineList3));
        assertTrue(machineGun.compatibleTargetSquares(playerD, machineGunList1));
        assertFalse(machineGun.compatibleTargetSquares(playerE, machineGunList1));
        assertTrue(twoShots.compatibleTargetSquares(playerG, machineGunList2));
        assertFalse(twoShots.compatibleTargetSquares(playerE, machineGunList1));
        assertFalse(twoShots.compatibleTargetSquares(playerF, machineGunList3));
    }

    @Test
    void checkSquareVisibility() {
        Target visibleTarg = new Target(1, Visibility.VISIBLE, -1, -1, false, false);
        Target invisibleTarg = new Target(4, Visibility.INVISIBLE, -1, -1, false, false);
        Target dcTarg = new Target(2, Visibility.DC, -1, -1, false, false);

        // Should be ok...
        assertTrue(visibleTarg.checkSquareVisibility(map[2][1], map[1][0]));
        assertFalse(visibleTarg.checkSquareVisibility(map[1][1], map[3][0]));
        assertTrue(invisibleTarg.checkSquareVisibility(map[3][2], map[0][2]));
        assertFalse(invisibleTarg.checkSquareVisibility(map[0][0], map[0][1]));
        assertTrue(dcTarg.checkSquareVisibility(map[2][1], map[2][0]));
        assertTrue(dcTarg.checkSquareVisibility(map[2][2], map[2][1]));
    }

    @Test
    void checkDistance() {
        Target everythingGoes = new Target(2, Visibility.DC, -1, -1, false, false);
        Target looooongOne = new Target(2, Visibility.DC, 3, -1, false, false);
        Target closeOne = new Target(2, Visibility.DC, -1, 0, false, false);
        Target twoStepsAway = new Target(2, Visibility.DC, 2, 2, false, false);

        // Also this one should be pretty easy to pass
        assertTrue(everythingGoes.checkDistance(map[0][0], map[3][2]));
        assertTrue(looooongOne.checkDistance(map[0][2], map[3][0]));
        assertFalse(looooongOne.checkDistance(map[1][2], map[1][2]));
        assertTrue(closeOne.checkDistance(map[0][0], map[0][0]));
        assertFalse(closeOne.checkDistance(map[3][1], map[3][2]));
        assertTrue(twoStepsAway.checkDistance(map[1][2], map[2][1]));
        assertFalse(twoStepsAway.checkDistance(map[0][2], map[0][1]));
    }
}