package it.polimi.ingsw.model.minified;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.util.Json;
import it.polimi.ingsw.model.SpawnPoint;
import it.polimi.ingsw.model.weapons.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import static org.junit.jupiter.api.Assertions.*;

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
    void serialization() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()){
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(miniSpawn);
            out.flush();
        }
    }

    @Test
    void jackson() throws IOException {
        String j = Json.getMapper().writeValueAsString(miniSpawn);
        MiniSpawnPoint fromJson = Json.getMapper().readValue(j, MiniSpawnPoint.class);
        assertEquals(miniSpawn.getUuid(), fromJson.getUuid());
    }
}