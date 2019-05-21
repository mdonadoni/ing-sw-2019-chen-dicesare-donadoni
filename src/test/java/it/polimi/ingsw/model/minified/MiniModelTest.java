package it.polimi.ingsw.model.minified;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.util.Json;
import it.polimi.ingsw.util.ResourceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiniModelTest {
    MiniModel miniModel;

    @BeforeEach
    void setUp() throws ResourceException {
        miniModel = new MiniModel(new Match(Arrays.asList("Gennaro", "Luca", "Marco"), BoardType.SMALL), new Player("Gennaro", PlayerToken.YELLOW));
    }

    @Test
    void getters() {
        assertEquals(miniModel.getMyNickname(), "Gennaro");
        assertEquals(miniModel.getMyPoints(), 0);
    }

    @Test
    void serialization() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()){
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(miniModel);
            out.flush();
        }
    }

    @Test
    void jackson() throws IOException {
        String j = Json.getMapper().writeValueAsString(miniModel);
        Json.getMapper().readValue(j, MiniModel.class);
    }
}