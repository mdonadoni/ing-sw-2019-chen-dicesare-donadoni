package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.StandardSquare;

/**
 * StandardSquare for the client.
 */
public class MiniStandardSquare extends MiniSquare {
    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = 6711202246787843113L;
    /**
     * Ammo tile inside this square (null if there is none).
     */
    private MiniAmmoTile ammoTile;

    /**
     * Constructor for jackson.
     */
    @JsonCreator
    private MiniStandardSquare() {
        ammoTile = null;
    }

    /**
     * Constructor of a MiniStandardSquare from a full StandardSquare.
     * @param square StandardSquare to be copied.
     */
    MiniStandardSquare(StandardSquare square) {
        super(square);
        ammoTile = square.hasAmmoTile() ? new MiniAmmoTile(square.getAmmoTile()) : null;
    }

    /**
     * Get whether this square has an ammo tile.
     * @return True if there is an ammo tile, otherwise false.
     */
    public boolean hasAmmo() {
        return ammoTile != null;
    }

    /**
     * Get the ammo tile inside this square.
     * @return The ammo tile inside this square, or null if there is none.
     */
    public MiniAmmoTile getAmmoTile() {
        return ammoTile;
    }
}
