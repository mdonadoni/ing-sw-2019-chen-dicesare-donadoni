package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Board board;

    @BeforeEach
    void setUp() {
        BoardSample bs = new BoardSample();
        board = bs.board;
    }

    @Test
    void getSquare() {
        assertNotNull(board.getSquare(new Coordinate(0, 0)));
    }

    @Test
    void getSquareThrow() {
        assertThrows(InvalidOperationException.class,
                () -> board.getSquare(new Coordinate(-1, 0)));
    }

    @Test
    void isSpawnPoint() {
        assertTrue(board.isSpawnPoint(new Coordinate(0, 0)));
        assertFalse(board.isSpawnPoint(new Coordinate(1, 0)));
    }

    @Test
    void isStandardSquare() {
        assertFalse(board.isStandardSquare(new Coordinate(0, 0)));
        assertTrue(board.isStandardSquare(new Coordinate(1, 0)));
    }

    @Test
    void getSpawPoint() {
        assertEquals(
                board.getSquare(new Coordinate(0, 0)),
                board.getSpawnPoint(new Coordinate(0, 0))
        );
    }

    @Test
    void getSpawPointThrow() {
        assertThrows(InvalidOperationException.class,
                () -> board.getSpawnPoint(new Coordinate(0, 1)));
    }

    @Test
    void getStandardSquare() {
        assertEquals(
                board.getSquare(new Coordinate(0, 1)),
                board.getStandardSquare(new Coordinate(0, 1))
        );
    }

    @Test
    void getStandardSquareThrow() {
        assertThrows(InvalidOperationException.class,
                () -> board.getStandardSquare(new Coordinate(0, 0)));
    }

    @Test
    void getSpawnPointByColor() {
        assertEquals(
                board.getSpawnPointByColor(AmmoColor.RED),
                board.getSpawnPoint(new Coordinate(0, 0))
        );
    }

    @Test
    void getSpawnPointByColorThrow() {
        assertThrows(InvalidOperationException.class,
                () -> board.getSpawnPointByColor(AmmoColor.BLUE));
    }

    @Test
    void readResources() throws ResourceException {
        for (BoardType type : BoardType.values()) {
            new Board(type);
        }
    }
}
