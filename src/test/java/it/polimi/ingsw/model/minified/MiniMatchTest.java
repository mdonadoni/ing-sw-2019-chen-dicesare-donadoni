package it.polimi.ingsw.model.minified;

import it.polimi.ingsw.common.UtilSerialization;
import it.polimi.ingsw.model.BoardType;
import it.polimi.ingsw.model.JsonModelFactory;
import it.polimi.ingsw.model.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

class MiniMatchTest {

    MiniMatch miniMatch;

    @BeforeEach
    void setUp() {
        miniMatch = new MiniMatch(new Match(Arrays.asList("a", "b", "c"), new JsonModelFactory(BoardType.SMALL)));
    }

    @Test
    void serialization() throws IOException, ClassNotFoundException {
        UtilSerialization.javaSerializable(miniMatch, MiniMatch.class);
    }

    @Test
    void jackson() throws IOException {
        UtilSerialization.jackson(miniMatch, MiniMatch.class);
    }
}