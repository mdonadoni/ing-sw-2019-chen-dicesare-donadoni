package it.polimi.ingsw.model.minified;

import it.polimi.ingsw.common.UtilSerialization;
import it.polimi.ingsw.model.BoardType;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.JsonModelFactory;
import it.polimi.ingsw.model.PlayerToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiniGameBoardTest {

    GameBoard gameBoard;
    MiniGameBoard miniGameBoard;

    @BeforeEach
    void setUp() {
        gameBoard = new GameBoard(5, new JsonModelFactory(BoardType.SMALL));
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
    void serialization() throws IOException, ClassNotFoundException {
        UtilSerialization.javaSerializable(miniGameBoard, MiniGameBoard.class);
    }

    @Test
    void jackson() throws IOException {
        UtilSerialization.jackson(miniGameBoard, MiniGameBoard.class);
    }
}