package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.AmmoTile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * AmmoTile for the client side.
 */
public class MiniAmmoTile implements Serializable {
    /**
     * Serializable UID
     */
    private static final long serialVersionUID = 7458609643541328777L;
    /**
     * List of ammo tile
     */
    private ArrayList<AmmoColor> ammo;
    /**
     * Define if it has a power-up
     */
    private boolean powerUp;

    @JsonCreator
    private MiniAmmoTile() {

    }

    /**
     * Constructor of the class
     * @param tile The AmmoTile to convert
     */
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
