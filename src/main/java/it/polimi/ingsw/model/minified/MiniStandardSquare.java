package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.StandardSquare;

public class MiniStandardSquare extends MiniSquare {

    private static final long serialVersionUID = 6711202246787843113L;

    private MiniAmmoTile ammoTile;

    /**
     * Constructor for jackson.
     */
    @JsonCreator
    private MiniStandardSquare() {
        ammoTile = null;
    }

    MiniStandardSquare(StandardSquare square) {
        super(square);
        ammoTile = square.hasAmmoTile() ? new MiniAmmoTile(square.getAmmoTile()) : null;
    }

    public boolean hasAmmo() {
        return ammoTile != null;
    }

    public MiniAmmoTile getAmmoTile() {
        return ammoTile;
    }
}
