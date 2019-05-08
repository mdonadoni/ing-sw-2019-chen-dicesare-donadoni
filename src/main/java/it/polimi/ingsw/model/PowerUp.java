package it.polimi.ingsw.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;

/**
 * This class represent the card power-up.
 */
public class PowerUp extends Identifiable implements Serializable {
    private static final long serialVersionUID = 1350606400736693235L;
    /**
     * The type of power-up.
     */
    private PowerUpType type;
    /**
     * The ammo that the power-up can be used as.
     */
    private AmmoColor ammo;

    /**
     * Constructor of the class
     * @param type the type of power-up.
     * @param ammo the ammo color of the power-up.
     */
    public PowerUp(PowerUpType type, AmmoColor ammo) {
        this.type = type;
        this.ammo = ammo;
    }

    public PowerUp(JsonNode json){
        type = PowerUpType.valueOf(json.get("type").asText().toUpperCase());
        ammo = AmmoColor.valueOf(json.get("color").asText().toUpperCase());
    }

    /**
     * Get the type of power-up.
     * @return the type of power-up.
     */
    public PowerUpType getType() {
        return type;
    }

    /**
     * Get the ammo color of the power-up.
     * @return the ammo color of the power-up.
     */
    public AmmoColor getAmmo() {
        return ammo;
    }
}
