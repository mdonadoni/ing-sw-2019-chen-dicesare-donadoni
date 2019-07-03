package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.PowerUp;
import it.polimi.ingsw.model.PowerUpType;

import java.io.Serializable;

public class MiniPowerUp extends MiniIdentifiable implements Serializable {
    private static final long serialVersionUID = 8145169883488823156L;
    AmmoColor ammo;
    PowerUpType type;

    @JsonCreator
    private MiniPowerUp() {
    }

    MiniPowerUp(PowerUp p) {
        super(p.getUuid());
        this.ammo = p.getAmmo();
        this.type = p.getType();
    }

    public AmmoColor getAmmo() {
        return ammo;
    }

    public PowerUpType getType() {
        return type;
    }
}
