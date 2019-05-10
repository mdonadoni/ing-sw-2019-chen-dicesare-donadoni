package it.polimi.ingsw.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;
import java.util.*;

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
        ammo = new ArrayList<>(ammo);
    }
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

     public AmmoTile(JsonNode json){
        ammo = new ArrayList<>();
        for(JsonNode color : json.get("ammos")){
            ammo.add(AmmoColor.valueOf(color.asText().toUpperCase()));
        }
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
