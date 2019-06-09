package it.polimi.ingsw.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents a tile of ammo.
 */
public class AmmoTile extends Identifiable implements Serializable {
    private static final long serialVersionUID = 5130130440250960678L;
    /**
     * List of ammo on the tile.
     */
    private List<AmmoColor> ammo;

    /**
     * Constructor for jackson.
     * @param ammo List of ammunition in AmmoTile
     */
    @JsonCreator
    private AmmoTile(@JsonProperty("ammo") List<AmmoColor> ammo) {
        this.ammo = new ArrayList<>(ammo);
    }

    /**
     * Constructor that make a tile made of 2 or 3 ammo
     * @param ammos ammunition colors
     */
    public AmmoTile(AmmoColor ...ammos){
        if (ammos.length < 2 || ammos.length > 3) {
            throw new InvalidOperationException("AmmoTile not valid");
        }
        ammo = new ArrayList<>(Arrays.asList(ammos));
    }

    /**
     * Add an ammo on a tile.
     * @param ammoColor the color of ammo to add.
     */
    public void addAmmo(AmmoColor ammoColor) {
        if(ammo.size()<3) {
            ammo.add(ammoColor);
        }
    }

    /**
     * Get the list of ammo.
     * @return the list of ammo.
     */
    public List<AmmoColor> getAmmo() {
        return new ArrayList<>(this.ammo);
    }

    /**
     * Get if the tile has a power-up
     * @return True if the tile has a power-up, false otherwise.
     */
    public boolean hasPowerUp(){
        return ammo.size()==2;
    }

    public int countAmmo(AmmoColor color){
        int res = 0;
        for(AmmoColor ammoCol : ammo){
            if(ammoCol.equals(color))
                res++;
        }
        return res;
    }
}
