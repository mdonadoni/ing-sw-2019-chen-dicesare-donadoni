package it.polimi.ingsw.model;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * This class represent the card power-up.
 */
public class PowerUp extends Identifiable {
    /**
     * The type of power-up.
     */
    private PowerUpType type;
    /**
     * The ammo that the power-up can be used as.
     */
    private AmmoColor ammo;

    /**
     * Standard constructor
     */
    @JsonCreator
    private PowerUp() {

    }

    /**
     * Constructor of the class
     * @param type the type of power-up.
     * @param ammo the ammo color of the power-up.
     */
    public PowerUp(PowerUpType type, AmmoColor ammo) {
        this.type = type;
        this.ammo = ammo;
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
