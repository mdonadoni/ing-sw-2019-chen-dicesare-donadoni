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

class MiniStandardSquareTest {

    StandardSquare square;
    MiniStandardSquare miniSquare;

    @BeforeEach
    void setUp() {
        square = new StandardSquare(new Coordinate(1, 2), new AmmoTile(AmmoColor.BLUE, AmmoColor.RED));
        miniSquare = new MiniStandardSquare(square);
    }

    @Test
    void getters() {
        assertEquals(miniSquare.getUuid(), square.getUuid());
        assertEquals(miniSquare.getCoordinates(), new Coordinate(1, 2));
        assertTrue(miniSquare.hasAmmo());
        assertTrue(miniSquare.getAmmoTile().hasPowerUp());
        assertEquals(miniSquare.getAmmoTile().getAmmo(), Arrays.asList(AmmoColor.BLUE, AmmoColor.RED));
        assertEquals(miniSquare.getPlayers().size(), 0);
    }

    @Test
    void serialization() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()){
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(miniSquare);
            out.flush();
        }
    }

    @Test
    void jackson() throws IOException {
        String j = Json.getMapper().writeValueAsString(miniSquare);
        MiniStandardSquare fromJson = Json.getMapper().readValue(j, MiniStandardSquare.class);
        assertEquals(miniSquare.getUuid(), fromJson.getUuid());
    }

}