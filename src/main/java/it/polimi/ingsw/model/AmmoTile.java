package it.polimi.ingsw.model;

import java.util.*;

/**
 * This class represents a tile of ammo.
 */
public class AmmoTile extends Identifiable{
    /**
     * List of ammo on the tile.
     */
    private List<AmmoColor> ammo;

    /**
     * Constructor that make a tile made of 3 ammo
     * @param ammoColor1 first ammo
     * @param ammoColor2 second ammo
     * @param ammoColor3 third ammo
     */
    public AmmoTile(AmmoColor ammoColor1, AmmoColor ammoColor2, AmmoColor ammoColor3){
        ammo = new ArrayList<>();
        ammo.add(ammoColor1);
        ammo.add(ammoColor2);
        ammo.add(ammoColor3);
    }

    /**
     * Constructor that make a tile made of 2 ammo
     * @param ammoColor1 first ammo
     * @param ammoColor2 second ammo
     */
    public AmmoTile(AmmoColor ammoColor1, AmmoColor ammoColor2){
        ammo = new ArrayList<>();
        ammo.add(ammoColor1);
        ammo.add(ammoColor2);
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
        return this.ammo;
    }

    /**
     * Get if the tile has a power-up
     * @return True if the tile has a power-up, false otherwise.
     */
    public boolean hasPowerUp(){
        return ammo.size()==2;
    }
}
