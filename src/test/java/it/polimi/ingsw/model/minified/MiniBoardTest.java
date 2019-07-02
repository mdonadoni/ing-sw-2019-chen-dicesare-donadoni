package it.polimi.ingsw.model.minified;

import it.polimi.ingsw.common.UtilSerialization;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.BoardSample;
import it.polimi.ingsw.model.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiniBoardTest {
    Board board;
    MiniBoard miniBoard;

    @BeforeEach
    void setUp() {
        board = new BoardSample().board;
        miniBoard = new MiniBoard(board);
    }

    @Test
    void getters() {
        assertEquals(miniBoard.getSpawnPoints().get(0).getColor(), board.getSpawnPoint(new Coordinate(0, 0)).getColor());
        assertEquals(miniBoard.getSpawnPoints().size(), 1);
        assertEquals(miniBoard.getStandardSquares().size(), 11);
    }

    @Test
    void serialization() throws IOException, ClassNotFoundException {
        UtilSerialization.javaSerializable(miniBoard, MiniBoard.class);
    }

    @Test
    void jackson() throws IOException {
        UtilSerialization.jackson(miniBoard, MiniBoard.class);
    }

}