package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StandardSquareTest {

    StandardSquare tile;
    StandardSquare noTile;
    AmmoTile ammo;

    @BeforeEach
    void setUp() {
        ammo = new AmmoTile(AmmoColor.RED,AmmoColor.BLUE);
        tile = new StandardSquare( new Coordinate(0,0), ammo);
        noTile = new StandardSquare(new Coordinate(0, 1));
    }

    @Test
    void getAmmoTile() {
        assertEquals(tile.getAmmoTile(), ammo);
    }

    @Test
    void setAmmoTile() {
        noTile.setAmmoTile(ammo);
        assertEquals(tile.getAmmoTile(), ammo);
    }

    @Test
    void removeAmmoTile() {
        tile.removeAmmoTile();
        assertFalse(tile.hasAmmoTile());
    }

    @Test
    void hasAmmoTile() {
        assertTrue(tile.hasAmmoTile());
        assertFalse(noTile.hasAmmoTile());
    }

    @Test
    void getAmmoTileThrow() {
        assertThrows(InvalidOperationException.class,
                () -> noTile.getAmmoTile());
    }

    @Test
    void setAmmoTileThrow() {
        assertThrows(InvalidOperationException.class,
                () -> tile.setAmmoTile(null));
    }

    @Test
    void removeAmmoTileThrow() {
        assertThrows(InvalidOperationException.class,
                () -> noTile.removeAmmoTile());
    }
}