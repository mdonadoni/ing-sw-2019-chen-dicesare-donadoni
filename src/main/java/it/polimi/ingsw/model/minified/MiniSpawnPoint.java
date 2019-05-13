package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.SpawnPoint;
import it.polimi.ingsw.model.weapons.Weapon;

import java.util.ArrayList;
import java.util.List;

public class MiniSpawnPoint extends MiniSquare {
    private static final long serialVersionUID = -2422198266664618608L;
    private final AmmoColor color;
    private final ArrayList<MiniWeapon> weapons;

    @JsonCreator
    private MiniSpawnPoint() {
        color = null;
        weapons = null;
    }

    MiniSpawnPoint(SpawnPoint spawn) {
        super(spawn);
        color = spawn.getColor();
        weapons = new ArrayList<>();
        for (Weapon w : spawn.getWeapons()) {
            weapons.add(new MiniWeapon(w));
        }
    }

    public AmmoColor getColor() {
        return color;
    }

    public List<MiniWeapon> getWeapons() {
        return new ArrayList<>(weapons);
    }
}
