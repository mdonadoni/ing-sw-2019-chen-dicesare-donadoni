package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTargetTest {
    int R, C;
    Square[][] map;
    List<List<Square>> rooms;

    // I know I know I should've used an array of players but I didn't want to fuck my mind up with numbers, it's
    // easier to manipulate testcases when players are identified with letters and cells with numbers.
    Player playerA = new Player("Alice");
    Player playerB = new Player("Bob");
    Player playerC = new Player("Charlie");
    Player playerD = new Player("Dean");
    Player playerE = new Player("Eva");
    Player playerF = new Player("Frank");
    Player playerG = new Player("Gary");

    private void createLink(Square a, Square b, LinkType type) {
        Link l = new Link(a, b, type);
        a.addLink(l);
        b.addLink(l);
    }

    void setUpBoard() {
        /* Construction of sample grid.
         * The grid is 4x3, with four rooms:
         *  - (0,0), (0,1), (1,0), (1,1)
         *  - (2,0), (3,0)
         *  - (0,2), (1,2)
         *  - (2,1), (2,2), (3,1), (3,2)
         *
         *  The doors are three:
         *  - (1,0), (2,0)
         *  - (1,1), (2,1)
         *  - (1,1), (1,2)
         */
        R = 4;
        C = 3;
        map = new Square[R][C];
        for (int i = 0; i < R; ++i) {
            for (int j = 0; j < C; ++j) {
                map[i][j] = new Square(new Coordinate(i, j));
            }
        }

        rooms = new ArrayList<>();
        List<Square> room;

        // Room 1
        room = new ArrayList<>();
        room.add(map[0][0]);
        room.add(map[0][1]);
        room.add(map[1][0]);
        room.add(map[1][1]);
        rooms.add(room);

        // Room 2
        room = new ArrayList<>();
        room.add(map[2][0]);
        room.add(map[3][0]);
        rooms.add(room);

        // Room 3
        room = new ArrayList<>();
        room.add(map[0][2]);
        room.add(map[1][2]);
        rooms.add(room);

        // Room 4
        room = new ArrayList<>();
        room.add(map[2][1]);
        room.add(map[2][2]);
        room.add(map[3][1]);
        room.add(map[3][2]);
        rooms.add(room);

        // Room 1
        room = new ArrayList<>();
        room.add(map[0][0]);
        room.add(map[0][1]);
        room.add(map[1][0]);
        room.add(map[1][1]);
        rooms.add(room);

        // Create links
        // Room 1
        createLink(map[0][0], map[0][1], LinkType.SAME_ROOM);
        createLink(map[0][0], map[1][0], LinkType.SAME_ROOM);
        createLink(map[1][1], map[0][1], LinkType.SAME_ROOM);
        createLink(map[1][1], map[1][0], LinkType.SAME_ROOM);
        // Room 2
        createLink(map[2][0], map[3][0], LinkType.SAME_ROOM);

        // Room 3
        createLink(map[0][2], map[1][2], LinkType.SAME_ROOM);

        // Room 4
        createLink(map[2][1], map[2][2], LinkType.SAME_ROOM);
        createLink(map[2][1], map[3][1], LinkType.SAME_ROOM);
        createLink(map[2][2], map[3][2], LinkType.SAME_ROOM);
        createLink(map[3][1], map[3][2], LinkType.SAME_ROOM);

        // Doors
        createLink(map[1][0], map[2][0], LinkType.DOOR);
        createLink(map[1][1], map[2][1], LinkType.DOOR);
        createLink(map[1][1], map[1][2], LinkType.DOOR);

        // Walls
        createLink(map[0][1], map[0][2], LinkType.WALL);
        createLink(map[1][2], map[2][2], LinkType.WALL);
        createLink(map[2][0], map[2][1], LinkType.WALL);
        createLink(map[3][0], map[3][1], LinkType.WALL);

    }
    void setUpBoardAndPlayers(){
        // Board
        setUpBoard();

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
        setUpBoardAndPlayers();

        // Setup targets
        PlayerTarget targetVis = new PlayerTarget(1, Visibility.VISIBLE, 1, -1, false, false);
        PlayerTarget targetInv = new PlayerTarget(1, Visibility.INVISIBLE, -1, 3, false, false);
        PlayerTarget longShot = new PlayerTarget(1, Visibility.VISIBLE, 2, -1, false, false);
        longShot.addSpecial(SpecialArea.LINE);
        PlayerTarget closeCombat = new PlayerTarget(1, Visibility.DC, -1, 0, false, false);

        // Let's see what happens...
        assertTrue(targetVis.validateTargetPlayer(playerA, playerB));
        assertFalse(targetVis.validateTargetPlayer(playerC, playerA));
        assertTrue(targetInv.validateTargetPlayer(playerB, playerA));
        assertFalse(targetInv.validateTargetPlayer(playerB, playerC));
        assertTrue(longShot.validateTargetPlayer(playerD, playerG));
        assertFalse(longShot.validateTargetPlayer(playerB, playerA));
        assertFalse(longShot.validateTargetPlayer(playerF, playerE));
        assertTrue(closeCombat.validateTargetPlayer(playerE, playerF));
        assertFalse(closeCombat.validateTargetPlayer(playerA, playerG));
    }

    @Test
    void compatibleTargetPlayers() {
        setUpBoardAndPlayers();

        // Setup targets
        PlayerTarget targetLine = new PlayerTarget(2, Visibility.DC, -1, 2, false, false);
        targetLine.addSpecial(SpecialArea.LINE);
        List<Player> targetLineList1 = new ArrayList<>();
        List<Player> targetLineList2 = new ArrayList<>();
        List<Player> targetLineList3 = new ArrayList<>();
        targetLineList1.add(playerD);
        targetLineList1.add(playerA);
        targetLineList2.add(playerA);
        targetLineList2.add(playerC);
        targetLineList3.add(playerA);

        PlayerTarget machineGun = new PlayerTarget(3, Visibility.VISIBLE, -1, 3, false, false);
        List<Player> machineGunList1 = new ArrayList<>();
        machineGunList1.add(playerA);
        machineGunList1.add(playerB);
        machineGunList1.add(playerG);

        PlayerTarget twoShots = new PlayerTarget(2, Visibility.INVISIBLE, -1, 3, false, false);
        List<Player> machineGunList2 = new ArrayList<>();
        machineGunList2.add(playerE);
        machineGunList2.add(playerF);

        List<Player> machineGunList3 = new ArrayList<>();
        machineGunList3.add(playerB);
        machineGunList3.add(playerD);

        // Dear Lord, make these tests pass
        assertTrue(targetLine.compatibleTargetPlayers(playerC, targetLineList1));
        assertFalse(targetLine.compatibleTargetPlayers(playerD, targetLineList2));
        assertTrue(targetLine.compatibleTargetPlayers(playerC, targetLineList3));
        assertTrue(machineGun.compatibleTargetPlayers(playerD, machineGunList1));
        assertFalse(machineGun.compatibleTargetPlayers(playerE, machineGunList1));
        assertTrue(twoShots.compatibleTargetPlayers(playerG, machineGunList2));
        assertFalse(twoShots.compatibleTargetPlayers(playerE, machineGunList1));
        assertFalse(twoShots.compatibleTargetPlayers(playerF, machineGunList3));
    }
}