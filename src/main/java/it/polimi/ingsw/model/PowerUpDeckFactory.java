package it.polimi.ingsw.model;

/**
 * Interface of the PowerUpDeckFactory.
 */
public interface PowerUpDeckFactory {
    /**
     * Create a deck of power-up, loading from a file that contains the different types of power-up then shuffle it.
     * @return The deck of power-up.
     */
    Deck<PowerUp> createPowerUpDeck();
}
