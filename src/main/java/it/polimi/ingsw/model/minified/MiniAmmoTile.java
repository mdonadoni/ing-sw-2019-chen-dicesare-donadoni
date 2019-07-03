package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.AmmoTile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MiniAmmoTile implements Serializable {
    private static final long serialVersionUID = 7458609643541328777L;
    private ArrayList<AmmoColor> ammo;
    private boolean powerUp;

    @JsonCreator
    private MiniAmmoTile() {

    }

    MiniAmmoTile(AmmoTile tile) {
        powerUp = tile.hasPowerUp();
        ammo = new ArrayList<>(tile.getAmmo());
    }

    public List<AmmoColor> getAmmo() {
        return new ArrayList<>(this.ammo);
    }

    public boolean hasPowerUp() {
        return powerUp;
    }
}
