package it.polimi.ingsw.model;

/**
 * Interface of the AmmoTileDeckFactory.
 */
public interface AmmoTileDeckFactory {
    /** Creates an AmmoTile deck
     * @return The AmmoTile deck created
     */
    Deck<AmmoTile> createAmmoTileDeck();
}
