package it.polimi.ingsw.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.model.weapons.WeaponType;
import it.polimi.ingsw.util.Json;

public class JsonWeaponDeckFactory implements WeaponDeckFactory {
    @Override
    public Deck<Weapon> createWeaponDeck(){
        Deck<Weapon> deck = new Deck<>();

        for(WeaponType weaponName : WeaponType.values())
            deck.add(new Weapon(weaponName));

        return deck;
    }
}
