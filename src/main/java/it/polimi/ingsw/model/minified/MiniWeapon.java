package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.Identifiable;
import it.polimi.ingsw.model.weapons.Weapon;

import java.io.Serializable;
import java.util.ArrayList;

public class MiniWeapon extends Identifiable implements Serializable {

    private static final long serialVersionUID = -5440998506651868180L;
    private final String name;
    private final boolean charged;
    private final ArrayList<AmmoColor> pickupColor;
    private final AmmoColor additionalRechargeColor;
    // TODO add attacks

    @JsonCreator
    private MiniWeapon() {
        name = null;
        charged = false;
        pickupColor = null;
        additionalRechargeColor = null;
    }

    MiniWeapon(Weapon weapon) {
        super(weapon.getUuid());
        this.name = weapon.getName();
        this.charged = weapon.getCharged();
        this.pickupColor = new ArrayList<>(weapon.getPickupColor());
        this.additionalRechargeColor = weapon.getAdditionalRechargeColor();
    }

    public String getName() {
        return name;
    }

    public boolean isCharged() {
        return charged;
    }

    public ArrayList<AmmoColor> getPickupColor() {
        return pickupColor;
    }

    public AmmoColor getAdditionalRechargeColor() {
        return additionalRechargeColor;
    }
}
