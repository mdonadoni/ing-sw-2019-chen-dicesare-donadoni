package it.polimi.ingsw.model;

import it.polimi.ingsw.common.UtilSerialization;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PowerUpTest {
    @Test
    void serialization() throws IOException, ClassNotFoundException {
        PowerUp p = new PowerUp(PowerUpType.NEWTON, AmmoColor.BLUE);
        PowerUp des = UtilSerialization.javaSerializable(p, PowerUp.class);
        assertEquals(des.getUuid(), p.getUuid());
    }

    @Test
    void jackson() throws IOException {
        PowerUp p = new PowerUp(PowerUpType.NEWTON, AmmoColor.BLUE);
        PowerUp des = UtilSerialization.jackson(p, PowerUp.class);
        assertEquals(des.getUuid(), p.getUuid());
    }
}