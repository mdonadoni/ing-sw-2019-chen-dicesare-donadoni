package it.polimi.ingsw.model.minified;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.util.Json;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MiniGameBoardTest {

    GameBoard gameBoard;
    MiniGameBoard miniGameBoard;

    @BeforeEach
    void setUp() throws ResourceException {
        gameBoard = new GameBoard(5, BoardType.SMALL);
        gameBoard.addKill(Arrays.asList(PlayerToken.BLUE, PlayerToken.YELLOW));
        gameBoard.addKill(Arrays.asList(PlayerToken.GREEN, PlayerToken.GREY));
        miniGameBoard = new MiniGameBoard(gameBoard);
    }

    @Test
    void getters() {
        assertEquals(miniGameBoard.getInitialSkullNumber(), 5);
        assertEquals(miniGameBoard.getKillShotTrack(), gameBoard.getKillShotTrack());
        assertEquals(miniGameBoard.getRemainingSkulls(), 3);
    }

    @Test
    void serialization() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()){
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(miniGameBoard);
            out.flush();
        }
    }

    @Test
    void jackson() throws IOException {
        String j = Json.getMapper().writeValueAsString(miniGameBoard);
        Json.getMapper().readValue(j, MiniGameBoard.class);
    }
}