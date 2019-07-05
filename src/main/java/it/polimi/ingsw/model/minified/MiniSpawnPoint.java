package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.SpawnPoint;
import it.polimi.ingsw.model.weapons.Weapon;

import java.util.ArrayList;
import java.util.List;

/**
 * SpawnPoint for the client side.
 */
public class MiniSpawnPoint extends MiniSquare {
    /**
     * Serializable ID.
     */
    private static final long serialVersionUID = -2422198266664618608L;
    /**
     * Color of the spawnpoint.
     */
    private final AmmoColor color;
    /**
     * List of weapons in the spawnpoint.
     */
    private final ArrayList<MiniWeapon> weapons;

    @JsonCreator
    private MiniSpawnPoint() {
        color = null;
        weapons = null;
    }

    /**
     * This class constructs a MiniSpawnPoint from a full SpawnPoint.
     * @param spawn Spawnpoint.
     */
    MiniSpawnPoint(SpawnPoint spawn) {
        super(spawn);
        color = spawn.getColor();
        weapons = new ArrayList<>();
        for (Weapon w : spawn.getWeapons()) {
            weapons.add(new MiniWeapon(w));
        }
    }

    /**
     * Get color of spawnpoint.
     * @return Color of spawnpoint.
     */
    public AmmoColor getColor() {
        return color;
    }

    /**
     * Get weapons in spawnpoint.
     * @return List of weapons in the spawnpoint.
     */
    public List<MiniWeapon> getWeapons() {
        return new ArrayList<>(weapons);
    }
}
