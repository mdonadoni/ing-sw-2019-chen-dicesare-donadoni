package it.polimi.ingsw.model;

import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.model.weapons.WeaponType;

public interface WeaponFactory {
    Weapon createWeapon(WeaponType type);
}
