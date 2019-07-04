package it.polimi.ingsw.model;

import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.model.weapons.WeaponType;
/**
 * This class represent the implementation of the WeaponDeckFactory.
 */
public class JsonWeaponDeckFactory implements WeaponDeckFactory {
    /**
     * Weapon factory to create the weapons.
     */
    JsonWeaponFactory weaponFactory = new JsonWeaponFactory();
    /**
     * Create a deck of weapon, loading from a file that contains the different types of weapon then shuffle it.
     * @return The deck of weapon.
     */
    @Override
    public Deck<Weapon> createWeaponDeck(){
        Deck<Weapon> deck = new Deck<>();

        for(WeaponType weaponName : WeaponType.values())
            deck.add(weaponFactory.createWeapon(weaponName));

        deck.shuffle();
        return deck;
    }
}
