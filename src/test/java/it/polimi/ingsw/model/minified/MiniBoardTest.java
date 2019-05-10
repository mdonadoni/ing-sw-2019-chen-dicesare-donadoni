package it.polimi.ingsw.model.minified;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.BoardSample;
import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.Json;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import static org.junit.jupiter.api.Assertions.*;

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
    void serialization() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()){
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(miniBoard);
            out.flush();
        }
    }

    @Test
    void jackson() throws IOException {
        String j = Json.getMapper().writeValueAsString(miniBoard);
        Json.getMapper().readValue(j, MiniBoard.class);
    }

}