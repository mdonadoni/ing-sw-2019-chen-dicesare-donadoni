package it.polimi.ingsw.model.minified;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerToken;
import it.polimi.ingsw.model.weapons.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MiniPlayerTest {
    Player player;
    MiniPlayer mp;
    MiniWeapon mw;

    @BeforeEach
    void setUp() {
        player = new Player("test", PlayerToken.YELLOW);
        player.grabAmmo(new AmmoTile(AmmoColor.BLUE, AmmoColor.RED));
        player.takeDamage(PlayerToken.BLUE, 2);
        player.takeDamage(PlayerToken.GREEN, 3);
        player.addMark(PlayerToken.GREY, 2);
        player.addMark(PlayerToken.PURPLE, 1);
        player.addSkull(); player.addSkull();
        Weapon w = new Weapon("asd");
        player.grabWeapon(w);
        mw = new MiniWeapon(w);
        mp = new MiniPlayer(player);
    }

    @Test
    void getters() {
        assertEquals(mp.getUuid(), player.getUuid());
        assertEquals(mp.getAmmo(), Arrays.asList(AmmoColor.BLUE, AmmoColor.RED));
        assertEquals(mp.getColor(), PlayerToken.YELLOW);
        assertEquals(mp.getDamageTaken(),
                Arrays.asList(
                        PlayerToken.BLUE,
                        PlayerToken.BLUE,
                        PlayerToken.GREEN,
                        PlayerToken.GREEN,
                        PlayerToken.GREEN));
        assertEquals(mp.getMarks(),
                Arrays.asList(
                        PlayerToken.GREY,
                        PlayerToken.GREY,
                        PlayerToken.PURPLE));
        assertEquals(mp.getNickname(), "test");
        assertEquals(mp.getSkulls(), 2);
        assertEquals(mp.getWeapons().get(0).getUuid(), mw.getUuid());
        assertTrue(mp.isActive());
        assertFalse(mp.isBoardFlipped());
    }

    @Test
    void serialization() throws IOException, ClassNotFoundException {
        MiniPlayer des = UtilSerialization.javaSerializable(mp, MiniPlayer.class);
        assertEquals(des.getUuid(), mp.getUuid());
    }

    @Test
    void jackson() throws IOException {
        MiniPlayer des = UtilSerialization.jackson(mp, MiniPlayer.class);
        assertEquals(des.getUuid(), mp.getUuid());
    }
}