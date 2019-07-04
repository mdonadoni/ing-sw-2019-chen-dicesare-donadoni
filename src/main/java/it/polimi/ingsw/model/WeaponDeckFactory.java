package it.polimi.ingsw.model;

import it.polimi.ingsw.model.weapons.Weapon;

/**
 * The interface of WeaponDeckFactory.
 */
public interface WeaponDeckFactory {
    /**
     * Create a deck of weapon, loading from a file that contains the different types of weapon then shuffle it.
     * @return The deck of weapon.
     */
    Deck<Weapon> createWeaponDeck();
}
