package it.polimi.ingsw.model;

public class JsonModelFactory implements ModelFactory {
    JsonAmmoTileDeckFactory ammoTileDeckFactory;
    JsonBoardFactory boardFactory;
    JsonPowerUpDeckFactory powerUpDeckFactory;

    public JsonModelFactory(BoardType type) {
        ammoTileDeckFactory = new JsonAmmoTileDeckFactory();
        boardFactory = new JsonBoardFactory(type);
        powerUpDeckFactory = new JsonPowerUpDeckFactory();
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
}
