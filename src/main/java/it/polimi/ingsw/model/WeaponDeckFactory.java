package it.polimi.ingsw.model;

import it.polimi.ingsw.model.weapons.Weapon;

public interface WeaponDeckFactory {
    Deck<Weapon> createWeaponDeck();
}
