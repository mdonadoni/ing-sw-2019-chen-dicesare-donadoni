package it.polimi.ingsw.model.minified;

import it.polimi.ingsw.common.UtilSerialization;
import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.JsonWeaponFactory;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.model.weapons.WeaponType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiniWeaponTest {
    MiniWeapon miniWeapon;
    Weapon weapon;
    JsonWeaponFactory weaponFactory;

    @BeforeEach
    void setUp() {
        weaponFactory = new JsonWeaponFactory();
        weapon = weaponFactory.createWeapon(WeaponType.WHISPER);
        miniWeapon = new MiniWeapon(weapon);
    }

    @Test
    void getters() {
        assertEquals(miniWeapon.getUuid(), weapon.getUuid());
        assertEquals(miniWeapon.getName(), "whisper");
        assertEquals(miniWeapon.getAdditionalRechargeColor(), AmmoColor.BLUE);
        assertEquals(miniWeapon.getPickupColor(), Arrays.asList(AmmoColor.BLUE, AmmoColor.YELLOW));
        assertEquals(miniWeapon.isCharged(), true);
    }

    @Test
    void serialization() throws IOException, ClassNotFoundException {
        MiniWeapon des = UtilSerialization.javaSerializable(miniWeapon, MiniWeapon.class);
        assertEquals(des.getUuid(), miniWeapon.getUuid());
    }

    @Test
    void jackson() throws IOException {
        MiniWeapon des = UtilSerialization.jackson(miniWeapon, MiniWeapon.class);
        assertEquals(des.getUuid(), miniWeapon.getUuid());
    }
}