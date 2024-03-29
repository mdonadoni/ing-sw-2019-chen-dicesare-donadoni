package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AmmoTileTest {
    AmmoTile ammoTile;

    @BeforeEach
    void setUp() {
        ammoTile=new AmmoTile(AmmoColor.RED, AmmoColor.YELLOW);
    }

    @Test
    void hasPowerUp() {
        assertTrue(ammoTile.hasPowerUp());
        ammoTile.addAmmo(AmmoColor.BLUE);
        assertFalse(ammoTile.hasPowerUp());
    }

    @Test
    void addAmmo() {
        ammoTile.addAmmo(AmmoColor.YELLOW);
        assertEquals(3,ammoTile.getAmmo().size());
        ammoTile.addAmmo(AmmoColor.YELLOW);
        assertEquals(3,ammoTile.getAmmo().size());
    }



}