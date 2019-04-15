package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class BoardSample {
    public Square map[][];
    public final int R = 4;
    public final int C = 3;
    public Board board;
    public List<List<Square>> rooms;

    public BoardSample() {
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
        map = new Square[R][C];
        board = new Board();
        for (int i = 0; i < R; ++i) {
            for (int j = 0; j < C; ++j) {
                if (i == 0 && j == 0) {
                    // red spawnpoint
                    board.addSpawnPoint(new Coordinate(0, 0), AmmoColor.RED);
                } else {
                    board.addStandardSquare(new Coordinate(i, j));
                }
                map[i][j] =  board.getSquare(new Coordinate(i, j));
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
        board.addLink(map[0][0], map[0][1], LinkType.SAME_ROOM);
        board.addLink(map[0][0], map[1][0], LinkType.SAME_ROOM);
        board.addLink(map[1][1], map[0][1], LinkType.SAME_ROOM);
        board.addLink(map[1][1], map[1][0], LinkType.SAME_ROOM);
        // Room 2
        board.addLink(map[2][0], map[3][0], LinkType.SAME_ROOM);

        // Room 3
        board.addLink(map[0][2], map[1][2], LinkType.SAME_ROOM);

        // Room 4
        board.addLink(map[2][1], map[2][2], LinkType.SAME_ROOM);
        board.addLink(map[2][1], map[3][1], LinkType.SAME_ROOM);
        board.addLink(map[2][2], map[3][2], LinkType.SAME_ROOM);
        board.addLink(map[3][1], map[3][2], LinkType.SAME_ROOM);

        // Doors
        board.addLink(map[1][0], map[2][0], LinkType.DOOR);
        board.addLink(map[1][1], map[2][1], LinkType.DOOR);
        board.addLink(map[1][1], map[1][2], LinkType.DOOR);

        // Walls
        board.addLink(map[0][1], map[0][2], LinkType.WALL);
        board.addLink(map[1][2], map[2][2], LinkType.WALL);
        board.addLink(map[2][0], map[2][1], LinkType.WALL);
        board.addLink(map[3][0], map[3][1], LinkType.WALL);

    }
}
