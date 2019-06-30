package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.Identifiable;
import it.polimi.ingsw.model.weapons.Attack;
import it.polimi.ingsw.model.weapons.Weapon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MiniWeapon extends Identifiable implements Serializable {

    private static final long serialVersionUID = -5440998506651868180L;
    private final String name;
    private final boolean charged;
    private final ArrayList<AmmoColor> pickupColor;
    private final AmmoColor additionalRechargeColor;
    private final List<MiniAttack> attacks;

    @JsonCreator
    private MiniWeapon() {
        name = null;
        charged = false;
        pickupColor = null;
        additionalRechargeColor = null;
        attacks = null;
    }

    MiniWeapon(Weapon weapon) {
        super(weapon.getUuid());
        this.name = weapon.getName();
        this.charged = weapon.isCharged();
        this.pickupColor = new ArrayList<>(weapon.getPickupColor());
        this.additionalRechargeColor = weapon.getAdditionalRechargeColor();
        attacks = new ArrayList<>();
        for(Attack atk : weapon.getAttacks())
            attacks.add(new MiniAttack(atk));
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
