package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.weapons.Attack;
import it.polimi.ingsw.model.weapons.Weapon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MiniWeapon extends MiniIdentifiable implements Serializable {

    private static final long serialVersionUID = -5440998506651868180L;
    private final String name;
    private final boolean charged;
    private final ArrayList<AmmoColor> pickupColor;
    private final AmmoColor additionalRechargeColor;
    private final ArrayList<MiniAttack> attacks;

    @JsonCreator
    private MiniWeapon() {
        name = null;
        charged = false;
        pickupColor = null;
        additionalRechargeColor = null;
        attacks = null;
    }

    public MiniWeapon(Weapon weapon) {
        super(weapon.getUuid());
        this.name = weapon.getName();
        this.charged = weapon.isCharged();
        this.pickupColor = new ArrayList<>(weapon.getPickupColor());
        this.additionalRechargeColor = weapon.getAdditionalRechargeColor();
        this.attacks = new ArrayList<>();
        visitAttacks(weapon.getAttacks());
    }

    private void visitAttacks(List<Attack> atks) {
        for (Attack atk : atks) {
            attacks.add(new MiniAttack(atk));
            if (atk.hasAdditionalAttacks()) {
                visitAttacks(atk.getAdditionalAttacks());
            }
        }
    }

    public String getName() {
        return name;
    }

    public boolean isCharged() {
        return charged;
    }

    public List<AmmoColor> getPickupColor() {
        return pickupColor;
    }

    public AmmoColor getAdditionalRechargeColor() {
        return additionalRechargeColor;
    }

    public List<MiniAttack> getAttacks() {
        return attacks;
    }
}
