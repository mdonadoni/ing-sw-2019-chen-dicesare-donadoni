package it.polimi.ingsw.model.minified;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.Json;
import it.polimi.ingsw.model.ResourceException;
import it.polimi.ingsw.model.weapons.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MiniWeaponTest {
    private MiniWeapon weapon;
    @BeforeEach
    void setUp() throws ResourceException {
        weapon = new MiniWeapon(
                new Weapon(MiniWeaponTest.class.getResourceAsStream("/weapons/testweapon.json")));
    }

    @Test
    void getters() {
        assertEquals(weapon.getName(), "testweapon");
        assertEquals(weapon.getAdditionalRechargeColor(), AmmoColor.BLUE);
        assertEquals(weapon.getPickupColor(), Arrays.asList(AmmoColor.BLUE, AmmoColor.RED));
        assertEquals(weapon.isCharged(), true);
    }

    @Test
    void serialization() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()){
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(weapon);
            out.flush();
        }
    }

    @Test
    void jackson() throws IOException {
        String j = Json.getMapper().writeValueAsString(weapon);
        Json.getMapper().readValue(j, MiniWeapon.class);
    }
}