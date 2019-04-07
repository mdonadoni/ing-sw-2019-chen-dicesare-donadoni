package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AmmoTileTest {
    @Test
    void hasPowerUp() {
        AmmoTile ammoTile=new AmmoTile(AmmoColor.RED, AmmoColor.YELLOW);
        assertTrue(ammoTile.hasPoweUp());
        ammoTile.addAmmo(AmmoColor.BLUE);
        assertFalse(ammoTile.hasPoweUp());

    }
}