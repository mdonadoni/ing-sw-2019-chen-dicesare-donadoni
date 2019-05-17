package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SquareTest {

    int R, C;
    Square[][] map;
    List<List<Square>> rooms;

    private void createLink(Square a, Square b, LinkType type) {
        Link l = new Link(a, b, type);
        a.addLink(l);
        b.addLink(l);
    }

    @BeforeEach
    void setUp() {
        BoardSample bs = new BoardSample();
        R = bs.R;
        C = bs.C;
        map = bs.map;
        rooms = bs.rooms;
    }

    @Test
    void addLinkThrowLinkAlreadyPresent() {
        assertThrows(InvalidLinkException.class,
            () ->  map[0][0].addLink(new Link(map[0][0], map[0][1], LinkType.WALL)));
    }

    @Test
    void getDistance() {
        // distance between (2,2) and rest of map
        int[][] distance = new int[][]{
            {4, 3, 4},
            {3, 2, 3},
            {4, 1, 0},
            {5, 2, 1}
        };

        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                assertEquals(map[2][2].getDistance(map[i][j]), distance[i][j]);
                assertEquals(map[i][j].getDistance(map[2][2]), distance[i][j]);
            }
        }
    }

    @Test
    void getRoomSquares() {
        for (List<Square> room : rooms) {
            for (Square s : room) {
                assertEquals(s.getRoomSquares().size(), room.size());
                assertTrue(s.getRoomSquares().containsAll(room));
            }
        }
    }

    @Test
    void getAlignedSquaresNorth() {
        List<Square> aligned = new ArrayList<>();
        aligned.add(map[3][2]);
        aligned.add(map[2][2]);
        aligned.add(map[1][2]);
        aligned.add(map[0][2]);
        assertEquals(map[3][2].getAlignedSquares(Cardinal.NORTH), aligned);
    }

    @Test
    void getAlignedSquaresWest() {
        List<Square> aligned = new ArrayList<>();
        aligned.add(map[2][1]);
        aligned.add(map[2][0]);
        assertEquals(map[2][1].getAlignedSquares(Cardinal.WEST), aligned);
    }

    @Test
    void getAlignedSquaresOnBorderEast() {
        List<Square> aligned = new ArrayList<>();
        aligned.add(map[0][2]);
        assertEquals(map[0][2].getAlignedSquares(Cardinal.EAST), aligned);
    }

    @Test
    void getVisibleSquares() {
        List<Square> visible = new ArrayList<>();
        visible.add(map[0][0]);
        visible.add(map[0][1]);
        visible.add(map[1][0]);
        visible.add(map[1][1]);
        visible.add(map[0][2]);
        visible.add(map[1][2]);
        visible.add(map[2][1]);
        visible.add(map[2][2]);
        visible.add(map[3][1]);
        visible.add(map[3][2]);
        assertEquals(map[1][1].getVisibleSquares().size(), 10);
        assertTrue(map[1][1].getVisibleSquares().containsAll(visible));
    }

    @Test
    void isSameRoom() {
        // for every room
        for (List<Square> room : rooms) {
            // for every square in room
            for (Square s : room) {
                // for every other square
                for (int i = 0; i < R; i++) {
                    for (int j = 0; j < C; ++j) {
                        if (room.contains(map[i][j])) {
                            // other square is in room
                            assertTrue(s.isSameRoom(map[i][j]));
                        } else {
                            assertFalse(s.isSameRoom(map[i][j]));
                        }
                    }
                }
            }
        }
    }

    @Test
    void isVisible() {
        // visible form (1,1)
        boolean[][] visible = new boolean[][] {
            {true, true, true},
            {true, true, true},
            {false, true, true},
            {false, true, true}
        };
        for (int i = 0; i < R; ++i) {
            for (int j = 0; j < C; ++j) {
                assertEquals(map[1][1].isVisible(map[i][j]), visible[i][j]);
            }
        }
    }

    @Test
    void isAligned() {
        assertTrue(map[3][2].isAligned(Cardinal.NORTH, map[3][2]));
        assertTrue(map[3][2].isAligned(Cardinal.NORTH, map[2][2]));
        assertTrue(map[3][2].isAligned(Cardinal.NORTH, map[1][2]));
        assertTrue(map[3][2].isAligned(Cardinal.NORTH, map[0][2]));
    }

    @Test
    void isSomehowAligned() {
        assertTrue(map[1][1].isAligned(map[2][1]));
        assertTrue(map[1][1].isAligned(map[0][1]));
        assertTrue(map[1][1].isAligned(map[1][2]));
        assertTrue(map[1][1].isAligned(map[3][1]));
        assertTrue(map[1][1].isAligned(map[1][0]));
        assertFalse(map[3][1].isAligned(map[2][2]));
        assertFalse(map[3][1].isAligned(map[2][0]));
        assertFalse(map[3][1].isAligned(map[0][0]));
    }

    @Test
    void getSquaresByDistance() {
        Square square = map[2][1];
        List<Square> distanceTwo = square.getSquaresByDistance(2);

        assertTrue(distanceTwo.contains(map[2][2]));
        assertTrue(distanceTwo.contains(map[3][2]));
        assertTrue(distanceTwo.contains(map[3][1]));
        assertTrue(distanceTwo.contains(map[1][1]));
        assertTrue(distanceTwo.contains(map[1][2]));
        assertTrue(distanceTwo.contains(map[1][0]));
        assertFalse(distanceTwo.contains(map[3][0]));
        assertFalse(distanceTwo.contains(map[0][0]));
        assertFalse(distanceTwo.contains(map[0][2]));
    }

    @Test
    void getSquaresByDistanceAligned() {
        Square square = map[0][1];
        List<Square> distanceTwoAligned = square.getSquaresByDistanceAligned(2);

        assertTrue(distanceTwoAligned.contains(map[0][0]));
        assertTrue(distanceTwoAligned.contains(map[1][1]));
        assertTrue(distanceTwoAligned.contains(map[2][1]));
        assertFalse(distanceTwoAligned.contains(map[2][0]));
        assertFalse(distanceTwoAligned.contains(map[1][2]));
        assertFalse(distanceTwoAligned.contains(map[3][1]));
    }
}