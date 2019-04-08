package it.polimi.ingsw.model;

/**
 * This class represent the card power-up.
 */
public class PowerUp {
    /**
     * Number that identify the power-up.
     */
    private int id;
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
     * @param id the id of the power-up.
     * @param type the type of power-up.
     * @param ammo the ammo color of the power-up.
     */
    public PowerUp(int id, PowerUpType type, AmmoColor ammo) {
        this.id = id;
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
