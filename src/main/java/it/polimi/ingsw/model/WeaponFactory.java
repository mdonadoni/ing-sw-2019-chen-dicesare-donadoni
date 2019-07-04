package it.polimi.ingsw.model;

import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.model.weapons.WeaponType;

/**
 * The interface of WeaponFactory
 */
public interface WeaponFactory {
    /**
     * Create a weapon based on its type. This method load the weapon from the file where it is contained.
     * @param type The type of weapon.
     * @return The weapon created.
     */
    Weapon createWeapon(WeaponType type);
}
