package it.polimi.ingsw.model;

import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.model.weapons.WeaponType;

public class JsonWeaponDeckFactory implements WeaponDeckFactory {

    JsonWeaponFactory weaponFactory = new JsonWeaponFactory();

    @Override
    public Deck<Weapon> createWeaponDeck(){
        Deck<Weapon> deck = new Deck<>();

        for(WeaponType weaponName : WeaponType.values())
            deck.add(weaponFactory.createWeapon(weaponName));

        deck.shuffle();
        return deck;
    }
}
