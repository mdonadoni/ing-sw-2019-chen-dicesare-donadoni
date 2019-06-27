package it.polimi.ingsw.model;

import it.polimi.ingsw.model.weapons.Weapon;

public class JsonModelFactory implements ModelFactory {
    JsonAmmoTileDeckFactory ammoTileDeckFactory;
    JsonBoardFactory boardFactory;
    JsonPowerUpDeckFactory powerUpDeckFactory;
    JsonWeaponDeckFactory weaponDeckFactory;

    public JsonModelFactory(BoardType type) {
        ammoTileDeckFactory = new JsonAmmoTileDeckFactory();
        boardFactory = new JsonBoardFactory(type);
        powerUpDeckFactory = new JsonPowerUpDeckFactory();
        weaponDeckFactory = new JsonWeaponDeckFactory();
    }

    @Override
    public Deck<AmmoTile> createAmmoTileDeck() {
        return ammoTileDeckFactory.createAmmoTileDeck();
    }

    @Override
    public Board createBoard() {
        return boardFactory.createBoard();
    }

    @Override
    public Deck<PowerUp> createPowerUpDeck() {
        return powerUpDeckFactory.createPowerUpDeck();
    }

    @Override
    public Deck<Weapon> createWeaponDeck() {
        return weaponDeckFactory.createWeaponDeck();
    }
}
