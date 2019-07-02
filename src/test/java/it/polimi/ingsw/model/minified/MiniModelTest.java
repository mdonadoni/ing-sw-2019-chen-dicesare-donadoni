package it.polimi.ingsw.model.minified;

import it.polimi.ingsw.common.UtilSerialization;
import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiniModelTest {
    MiniModel miniModel;

    @BeforeEach
    void setUp() {
        miniModel = new MiniModel(new Match(Arrays.asList("Gennaro", "Luca", "Marco"), new JsonModelFactory(BoardType.SMALL)), new Player("Gennaro", PlayerToken.YELLOW));
    }

    @Test
    void getters() {
        assertEquals(miniModel.getMyNickname(), "Gennaro");
        assertEquals(miniModel.getMyPoints(), 0);
    }

    @Test
    void serialization() throws IOException, ClassNotFoundException {
        UtilSerialization.javaSerializable(miniModel, MiniModel.class);
    }

    @Test
    void jackson() throws IOException {
        UtilSerialization.jackson(miniModel, MiniModel.class);
    }
}