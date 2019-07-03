package it.polimi.ingsw.model.minified;

import it.polimi.ingsw.common.UtilSerialization;
import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.AmmoTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiniAmmoTileTest {
    MiniAmmoTile ammoTile;

    @BeforeEach
    void setUp() {
        ammoTile =  new MiniAmmoTile(new AmmoTile(AmmoColor.RED, AmmoColor.YELLOW));
    }
    @Test
    void serialization() throws IOException, ClassNotFoundException {
        MiniAmmoTile des = UtilSerialization.javaSerializable(ammoTile, MiniAmmoTile.class);
        assertEquals(ammoTile.getAmmo(), des.getAmmo());
        assertEquals(ammoTile.hasPowerUp(), des.hasPowerUp());
    }

    @Test
    void jackson() throws IOException {
        MiniAmmoTile des = UtilSerialization.jackson(ammoTile, MiniAmmoTile.class);
        assertEquals(ammoTile.getAmmo(), des.getAmmo());
        assertEquals(ammoTile.hasPowerUp(), des.hasPowerUp());
    }
}