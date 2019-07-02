package it.polimi.ingsw.model.minified;

import it.polimi.ingsw.common.UtilSerialization;
import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.SpawnPoint;
import it.polimi.ingsw.model.weapons.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiniSpawnPointTest {

    SpawnPoint spawn;
    MiniSpawnPoint miniSpawn;

    @BeforeEach
    void setUp() {
        spawn = new SpawnPoint(new Coordinate(0, 0), AmmoColor.YELLOW);
        spawn.addWeapon(new Weapon("test"));
        miniSpawn = new MiniSpawnPoint(spawn);
    }

    @Test
    void getters() {
        assertEquals(miniSpawn.getUuid(), spawn.getUuid());
        assertEquals(miniSpawn.getColor(), spawn.getColor());
        assertEquals(miniSpawn.getWeapons().get(0).getUuid(), spawn.getWeapons().get(0).getUuid());
        assertEquals(miniSpawn.getCoordinates(), spawn.getCoordinates());
    }

    @Test
    void serialization() throws IOException, ClassNotFoundException {
        MiniSpawnPoint des = UtilSerialization.javaSerializable(miniSpawn, MiniSpawnPoint.class);
        assertEquals(des.getUuid(), spawn.getUuid());
    }

    @Test
    void jackson() throws IOException {
        MiniSpawnPoint des = UtilSerialization.jackson(miniSpawn, MiniSpawnPoint.class);
        assertEquals(des.getUuid(), spawn.getUuid());
    }
}