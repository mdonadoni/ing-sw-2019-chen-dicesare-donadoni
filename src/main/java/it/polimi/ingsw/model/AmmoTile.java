package it.polimi.ingsw.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents a tile of ammo.
 */
public class AmmoTile {
    /**
     * Maximum amount of ammo.
     */
    private static final int MAX_AMMO = 3;
    /**
     * Minimum amount of ammo.
     */
    private static final int MIN_AMMO = MAX_AMMO - 1;
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
        if (ammos.length < MIN_AMMO || ammos.length > MAX_AMMO) {
            throw new InvalidOperationException("AmmoTile not valid");
        }
        ammo = new ArrayList<>(Arrays.asList(ammos));
    }

    /**
     * Add an ammo on a tile.
     * @param ammoColor the color of ammo to add.
     */
    public void addAmmo(AmmoColor ammoColor) {
        if(ammo.size() < MAX_AMMO) {
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
        return ammo.size() == MIN_AMMO;
    }
    /**
     * This method count the number of ammo by a color for the client side
     * @param color The color of ammo to count
     * @return The number of ammo by color
     */
    public int countAmmo(AmmoColor color){
        int res = 0;
        for(AmmoColor ammoCol : ammo){
            if(ammoCol.equals(color))
                res++;
        }
        return res;
    }
}
