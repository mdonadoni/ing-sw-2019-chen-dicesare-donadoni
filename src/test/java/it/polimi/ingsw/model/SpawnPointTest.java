package it.polimi.ingsw.model;

import it.polimi.ingsw.model.weapons.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpawnPointTest {
    SpawnPoint sp;

    @BeforeEach
    void setUp() {
        sp = new SpawnPoint(new Coordinate(0, 1), AmmoColor.RED);
    }

    @Test
    void getWeapons() {
        List<Weapon> weapons = new ArrayList<>();
        weapons.add(new Weapon("w1"));
        weapons.add(new Weapon("w2"));
        weapons.add(new Weapon("w3"));
        for (Weapon w : weapons) {
            sp.addWeapon(w);
        }

        assertEquals(sp.getWeapons().size(), weapons.size());
        assertTrue(sp.getWeapons().containsAll(weapons));
    }

    @Test
    void removeWeapon() {
        Weapon w = new Weapon("w1");
        sp.addWeapon(w);
        sp.removeWeapon(w);
        assertTrue(sp.getWeapons().isEmpty());
    }

    @Test
    void removeWeaponThrow() {
        assertThrows(InvalidOperationException.class,
                () -> sp.removeWeapon(new Weapon("w1")));
    }

    @Test
    void addWeaponThrow() {
        sp.addWeapon(new Weapon("w1"));
        sp.addWeapon(new Weapon("w2"));
        sp.addWeapon(new Weapon("w3"));
        assertThrows(InvalidOperationException.class,
                () -> sp.addWeapon(new Weapon("w4")));
    }

    @Test
    void countWeapons() {
        sp.addWeapon(new Weapon("w1"));
        sp.addWeapon(new Weapon("w2"));
        assertEquals(sp.countWeapons(), 2);
    }
}