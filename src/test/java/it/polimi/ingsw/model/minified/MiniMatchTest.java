package it.polimi.ingsw.model.minified;

import it.polimi.ingsw.model.BoardType;
import it.polimi.ingsw.model.JsonModelFactory;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.util.Json;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Arrays;

class MiniMatchTest {

    MiniMatch miniMatch;

    @BeforeEach
    void setUp() {
        miniMatch = new MiniMatch(new Match(Arrays.asList("a", "b", "c"), new JsonModelFactory(BoardType.SMALL)));
    }

    @Test
    void serialization() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()){
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(miniMatch);
            out.flush();
        }
    }

    @Test
    void jackson() throws IOException {
        String j = Json.getMapper().writeValueAsString(miniMatch);
        Json.getMapper().readValue(j, MiniMatch.class);
    }
}