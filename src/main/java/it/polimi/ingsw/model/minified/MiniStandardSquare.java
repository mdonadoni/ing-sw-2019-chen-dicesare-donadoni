package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.StandardSquare;

public class MiniStandardSquare extends MiniSquare {

    private static final long serialVersionUID = 6711202246787843113L;

    private AmmoTile ammoTile;

    /**
     * Constructor for jackson.
     */
    @JsonCreator
    MiniStandardSquare() {
        ammoTile = null;
    }

    MiniStandardSquare(StandardSquare square) {
        super(square);
        ammoTile = square.hasAmmoTile() ? square.getAmmoTile() : null;
    }

    public boolean hasAmmo() {
        return ammoTile != null;
    }

    public AmmoTile getAmmoTile() {
        return ammoTile;
    }
}
