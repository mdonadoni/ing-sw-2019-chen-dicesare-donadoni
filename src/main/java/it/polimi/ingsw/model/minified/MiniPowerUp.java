package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.PowerUp;
import it.polimi.ingsw.model.PowerUpType;

import java.io.Serializable;

/**
 * Powerup for the client.
 */
public class MiniPowerUp extends MiniIdentifiable implements Serializable {
    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = 8145169883488823156L;
    /**
     * Color of this powerup.
     */
    AmmoColor ammo;
    /**
     * Type of this powerup.
     */
    PowerUpType type;

    @JsonCreator
    private MiniPowerUp() {
    }

    /**
     * Constructor of a MiniPowerUp from a full PowerUp
     * @param p Powerup to be copied.
     */
    MiniPowerUp(PowerUp p) {
        super(p.getUuid());
        this.ammo = p.getAmmo();
        this.type = p.getType();
    }

    /**
     * Get color of this powerup.
     * @return Color of this powerup.
     */
    public AmmoColor getAmmo() {
        return ammo;
    }

    /**
     * Get type of this powerup.
     * @return Type of this powerup.
     */
    public PowerUpType getType() {
        return type;
    }
}
