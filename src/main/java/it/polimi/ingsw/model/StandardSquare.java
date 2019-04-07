package it.polimi.ingsw.model;

/**
 * This class represents a standard square (a square that contains an ammoTile).
 */
public class StandardSquare extends Square {
    /**
     * AmmoTile inside the square.
     */
    private AmmoTile ammoTile;

    /**
     * Constructor that makes a StandardSquare with no AmmoTile.
     * @param coord Coordinates of the square.
     */
    public StandardSquare(Coordinate coord) {
        super(coord);
        this.ammoTile = null;
    }

    /**
     * Constructor that makes a StandardSquare with given AmmoTile.
     * @param coord Coordinate of the square.
     * @param ammoTile AmmoTile inside the square.
     */
    public StandardSquare(Coordinate coord, AmmoTile ammoTile) {
        super(coord);
        this.ammoTile = ammoTile;
    }

    /**
     * Get AmmoTile inside the square.
     * @return Ammotile inside this square.
     */
    public AmmoTile getAmmoTile() {
        if (ammoTile == null) {
            throw new InvalidOperationException("There is no tile inside the square");
        }
        return ammoTile;
    }

    /**
     * Set AmmoTile inside the square.
     * @param ammo AmmoTile to be set.
     */
    public void setAmmoTile(AmmoTile ammo) {
        if (ammoTile != null) {
            throw new InvalidOperationException("Square already has AmmoTile");
        }
        ammoTile = ammo;
    }

    /**
     * Remove the AmmoTile inside the square.
     */
    public void removeAmmoTile() {
        if (ammoTile == null) {
            throw new InvalidOperationException("There is no tile to be removed");
        }
        ammoTile = null;
    }

    /**
     * Check if this square has an AmmoTile inside.
     * @return True if this square has an AmmoTile, otherwise false.
     */
    public boolean hasAmmoTile() {
        return (ammoTile != null);
    }
}
