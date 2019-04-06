package it.polimi.ingsw.model;

import java.util.Objects;

/**
 * This class that maintains the coordinates of a square.
 */
public class Coordinate {
    /**
     * Row coordinate.
     */
    private int row;

    /**
     * Column coordinate.
     */
    private int column;

    /**
     * Constructor of Coordinate.
     * @param row Row coordinate.
     * @param column Column coordinate.
     */
    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Get row coordinate.
     * @return Row coordinate.
     */
    public int getRow() {
        return row;
    }

    /**
     * Get column coordinate.
     * @return Column coordinate.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Hash function for Coordinate.
     * @return Hash of this coordinates.
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    /**
     * Check if two coordinates are equal. Two coordinates are equal if they
     * have the same row and column coordinates.
     * @param obj Other coordinate to be checked.
     * @return True if the two coordinates are equal, otherwise false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Coordinate) {
            Coordinate o = (Coordinate)obj;
            return this.row == o.row && this.column == o.column;
        }
        return false;
    }
}
