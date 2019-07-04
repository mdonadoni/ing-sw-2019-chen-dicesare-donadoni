package it.polimi.ingsw.model;

import it.polimi.ingsw.model.weapons.Weapon;
/**
 * This class represent the implementation of the ModelFactory which include the board
 * and the decks(weapon, power-up, ammo tile).
 */
public class JsonModelFactory implements ModelFactory {
    /**
     * The factory of the ammo tile deck.
     */
    JsonAmmoTileDeckFactory ammoTileDeckFactory;
    /**
     * The factory of the board.
     */
    JsonBoardFactory boardFactory;
    /**
     * The factory of the power-up deck.
     */
    JsonPowerUpDeckFactory powerUpDeckFactory;
    /**
     * The factory of the weapon deck.
     */
    JsonWeaponDeckFactory weaponDeckFactory;

    /**
     * Constructor of the class.
     * @param type The type of the board to generate.
     */
    public JsonModelFactory(BoardType type) {
        ammoTileDeckFactory = new JsonAmmoTileDeckFactory();
        boardFactory = new JsonBoardFactory(type);
        powerUpDeckFactory = new JsonPowerUpDeckFactory();
        weaponDeckFactory = new JsonWeaponDeckFactory();
    }

    /**
     * Create the ammo tile deck.
     * @return The ammo tile deck.
     */
    @Override
    public Deck<AmmoTile> createAmmoTileDeck() {
        return ammoTileDeckFactory.createAmmoTileDeck();
    }

    /**
     * Create the board.
     * @return The board.
     */
    @Override
    public Board createBoard() {
        return boardFactory.createBoard();
    }

    /**
     * Create the power-up deck.
     * @return The power-up deck.
     */
    @Override
    public Deck<PowerUp> createPowerUpDeck() {
        return powerUpDeckFactory.createPowerUpDeck();
    }

    /**
     * Create the weapon deck.
     * @return The weapon deck.
     */
    @Override
    public Deck<Weapon> createWeaponDeck() {
        return weaponDeckFactory.createWeaponDeck();
    }
}
