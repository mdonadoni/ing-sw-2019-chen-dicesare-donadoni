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
}