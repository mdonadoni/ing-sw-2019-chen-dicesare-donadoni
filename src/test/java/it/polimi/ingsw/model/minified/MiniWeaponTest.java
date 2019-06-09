package it.polimi.ingsw.model.minified;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.util.Json;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiniWeaponTest {
    MiniWeapon miniWeapon;
    Weapon weapon;

    @BeforeEach
    void setUp() {
        weapon = new Weapon(MiniWeaponTest.class.getResourceAsStream("/weapons/testweapon.json"));
        miniWeapon = new MiniWeapon(weapon);
    }

    @Test
    void getters() {
        assertEquals(miniWeapon.getUuid(), weapon.getUuid());
        assertEquals(miniWeapon.getName(), "testweapon");
        assertEquals(miniWeapon.getAdditionalRechargeColor(), AmmoColor.BLUE);
        assertEquals(miniWeapon.getPickupColor(), Arrays.asList(AmmoColor.BLUE, AmmoColor.RED));
        assertEquals(miniWeapon.isCharged(), true);
    }

    @Test
    void serialization() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()){
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(miniWeapon);
            out.flush();
        }
    }

    @Test
    void jackson() throws IOException {
        String j = Json.getMapper().writeValueAsString(miniWeapon);
        MiniWeapon fromJson = Json.getMapper().readValue(j, MiniWeapon.class);
        assertEquals(miniWeapon.getUuid(), fromJson.getUuid());
    }
}