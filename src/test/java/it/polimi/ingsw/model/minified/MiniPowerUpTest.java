package it.polimi.ingsw.model.minified;

import it.polimi.ingsw.common.UtilSerialization;
import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.PowerUp;
import it.polimi.ingsw.model.PowerUpType;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiniPowerUpTest {
    @Test
    void serialization() throws IOException, ClassNotFoundException {
        MiniPowerUp p = new MiniPowerUp(new PowerUp(PowerUpType.NEWTON, AmmoColor.BLUE));
        MiniPowerUp des = UtilSerialization.javaSerializable(p, MiniPowerUp.class);
        assertEquals(des.getUuid(), p.getUuid());
    }

    @Test
    void jackson() throws IOException {
        MiniPowerUp p = new MiniPowerUp(new PowerUp(PowerUpType.NEWTON, AmmoColor.BLUE));
        MiniPowerUp des = UtilSerialization.jackson(p, MiniPowerUp.class);
        assertEquals(des.getUuid(), p.getUuid());
    }
}