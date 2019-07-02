package it.polimi.ingsw.model.minified;

import it.polimi.ingsw.common.UtilSerialization;
import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.StandardSquare;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void serialization() throws IOException, ClassNotFoundException {
        MiniStandardSquare des = UtilSerialization.javaSerializable(miniSquare, MiniStandardSquare.class);
        assertEquals(des.getUuid(), miniSquare.getUuid());
    }

    @Test
    void jackson() throws IOException {
        MiniStandardSquare des = UtilSerialization.jackson(miniSquare, MiniStandardSquare.class);
        assertEquals(des.getUuid(), miniSquare.getUuid());
    }
}