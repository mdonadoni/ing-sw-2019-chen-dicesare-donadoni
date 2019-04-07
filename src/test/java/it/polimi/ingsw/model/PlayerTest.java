package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player player;

    @BeforeEach
    void setUp() {
        player=new Player("test");
    }

    @Test
    void takeDamage() {
        player.takeDamage(PlayerToken.BLUE,1);
        assertEquals(1, player.getDamageOrder().size());
        player.addMark(PlayerToken.BLUE,3);
        player.addMark(PlayerToken.GREEN,3);
        player.takeDamage(PlayerToken.BLUE,1);
        assertEquals(5, player.getDamageOrder().size());
        assertEquals(0, player.countMarks(PlayerToken.BLUE));
        assertEquals(3, player.countMarks(PlayerToken.GREEN));
        player.takeDamage(PlayerToken.BLUE,10);
        assertEquals(12, player.getDamageOrder().size());
    }

    @Test
    void addMark() {
        player.addMark(PlayerToken.BLUE,2);
        assertEquals(2, player.countMarks(PlayerToken.BLUE));
        player.addMark(PlayerToken.BLUE, 2);
        assertEquals(3, player.countMarks(PlayerToken.BLUE));
    }

    @Test
    void countMarks() {
        player.addMark(PlayerToken.GREEN, 3);
        assertEquals(3, player.countMarks(PlayerToken.GREEN));
        player.addMark(PlayerToken.GREY,2);
        assertEquals(2, player.countMarks(PlayerToken.GREY));
        player.addMark(PlayerToken.YELLOW,4);
        assertEquals(3, player.countMarks(PlayerToken.YELLOW));
    }

    @Test
    void grabAmmo() {
        AmmoTile ammoTile=new AmmoTile(AmmoColor.BLUE,AmmoColor.BLUE,AmmoColor.RED);
        player.grabAmmo(ammoTile);
        //assertTrue(player.);
        assertEquals(1, player.countAmmo(AmmoColor.RED));
        ammoTile=new AmmoTile(AmmoColor.BLUE,AmmoColor.BLUE);
        player.grabAmmo(ammoTile);
        assertEquals(3, player.countAmmo(AmmoColor.BLUE));
    }

    @Test
    void countAmmo() {
        AmmoTile ammoTile=new AmmoTile(AmmoColor.YELLOW,AmmoColor.YELLOW,AmmoColor.BLUE);
        player.grabAmmo(ammoTile);
        assertEquals(2, player.countAmmo(AmmoColor.YELLOW));
        assertEquals(1, player.countAmmo(AmmoColor.BLUE));

    }

    @Test
    void removeAmmo() {
        AmmoTile ammoTile=new AmmoTile(AmmoColor.YELLOW,AmmoColor.YELLOW,AmmoColor.BLUE);
        player.grabAmmo(ammoTile);
        player.removeAmmo(AmmoColor.YELLOW,1);
        assertEquals(1, player.countAmmo(AmmoColor.YELLOW));
        assertEquals(1, player.countAmmo(AmmoColor.BLUE));
        player.removeAmmo(AmmoColor.YELLOW,2);
        assertEquals(0, player.countAmmo(AmmoColor.YELLOW));
    }

    @Test
    void grabWeapon() {
    }
}