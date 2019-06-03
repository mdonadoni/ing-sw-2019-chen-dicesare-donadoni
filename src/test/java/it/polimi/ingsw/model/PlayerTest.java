package it.polimi.ingsw.model;

import it.polimi.ingsw.model.weapons.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player player;

    @BeforeEach
    void setUp() {
        player=new Player("test", PlayerToken.YELLOW);
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
        assertThrows(InvalidOperationException.class, ()->player.removeAmmo(AmmoColor.YELLOW,2));
    }

    @Test
    void grabWeapon() {
        Weapon weapon=new Weapon("THOR");
        player.grabWeapon(weapon);
        assertEquals(weapon ,player.getWeapons().get(0));
        assertEquals(1,player.getWeapons().size());
        player.grabWeapon(new Weapon("Vortex"));
        player.grabWeapon(new Weapon("Flamethrower"));
        player.grabWeapon(new Weapon("Laserblade"));
        assertEquals(3,player.getWeapons().size());
    }

    @Test
    void move(){
        BoardSample bs = new BoardSample();
        player.setSquare(bs.map[0][0]);
        bs.map[0][0].addPlayer(player);
        player.move(bs.map[2][2]);

        assertEquals(bs.map[0][0].getPlayers().size(), 0);
        assertEquals(player.getSquare(), bs.map[2][2]);
        assertEquals(bs.map[2][2].getPlayers().get(0), player);
    }

    @Test
    void supplyActions() {
        assertEquals(player.supplyActions(false).size(), 4);
        player.takeDamage(PlayerToken.GREY, 3);
        assertEquals(player.supplyActions(false).size(), 4);
        player.takeDamage(PlayerToken.GREY, 4);
        assertEquals(player.supplyActions(false).size(), 4);
    }

    @Test
    void canPay() {
        List<AmmoColor> cost = new ArrayList<>();
        cost.add(AmmoColor.BLUE);
        cost.add(AmmoColor.YELLOW);
        cost.add(AmmoColor.RED);
        cost.add(AmmoColor.YELLOW);

        player.addAmmo(AmmoColor.YELLOW);
        player.addAmmo(AmmoColor.RED);

        assertFalse(player.canPay(cost));

        player.addAmmo(AmmoColor.RED);
        player.addAmmo(AmmoColor.RED);

        assertFalse(player.canPay(cost));

        player.addAmmo(AmmoColor.BLUE);
        player.addAmmo(AmmoColor.YELLOW);

        assertTrue(player.canPay(cost));

        player.removeAmmo(AmmoColor.YELLOW, 2);
        player.addPowerUp(new PowerUp(PowerUpType.NEWTON, AmmoColor.YELLOW));
        player.addPowerUp(new PowerUp(PowerUpType.NEWTON, AmmoColor.YELLOW));

        assertTrue(player.canPay(cost));

        player.removeAmmo(AmmoColor.RED, 2);
        player.removeAmmo(AmmoColor.BLUE, 1);

        player.addPowerUp(new PowerUp(PowerUpType.NEWTON, AmmoColor.BLUE));
        cost.remove(AmmoColor.RED);

        assertTrue(player.canPay(cost));
    }

    @Test
    void payCost() {
        List<AmmoColor> cost = new ArrayList<>();
        cost.add(AmmoColor.RED);
        cost.add(AmmoColor.BLUE);
        cost.add(AmmoColor.RED);

        player.addAmmo(AmmoColor.YELLOW);
        player.addAmmo(AmmoColor.RED);
        player.addAmmo(AmmoColor.RED);
        player.addAmmo(AmmoColor.BLUE);

        player.payCost(cost);
        // YRRB - RBR => Y
        assertEquals(player.getAmmo().get(0), AmmoColor.YELLOW);

        player.addAmmo(AmmoColor.RED);
        player.addAmmo(AmmoColor.RED);
        player.addPowerUp(new PowerUp(PowerUpType.NEWTON, AmmoColor.BLUE));
        player.addPowerUp(new PowerUp(PowerUpType.NEWTON, AmmoColor.RED));

        player.payCost(cost);
        // YRR[B][R] - RBR => Y[R]
        assertEquals(player.getAmmo().get(0), AmmoColor.YELLOW);
        assertEquals(player.getPowerUps().get(0).getAmmo(), AmmoColor.RED);

        player.addAmmo(AmmoColor.YELLOW);
        player.addPowerUp(new PowerUp(PowerUpType.NEWTON, AmmoColor.BLUE));
        player.addPowerUp(new PowerUp(PowerUpType.NEWTON, AmmoColor.RED));

        player.payCost(cost);
        // YY[R][R][B] - RBR => YY
        assertTrue(player.getPowerUps().isEmpty());
        assertEquals(player.getAmmo().size(), 2);
        assertEquals(player.getAmmo().get(1), AmmoColor.YELLOW);
    }
}