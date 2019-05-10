package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.*;

import java.io.Serializable;

public class MiniSquare extends Identifiable implements Serializable {
    private static final long serialVersionUID = 2278398549083364767L;

    private final Coordinate coord;
    private final LinkType[] boundary;

    /**
     * Constructor for jackson.
     */
    @JsonCreator
    MiniSquare() {
        coord = null;
        boundary = null;
    }

    MiniSquare(Square square) {
        super(square.getUuid());
        this.coord = square.getCoordinates();
        boundary = new LinkType[4];
        for (int i = 0; i < 4; i++) {
            boundary[i] = LinkType.WALL;
        }
        for (Link l : square.getLinks()) {
            boundary[l.getDirectionFromSquare(square).ordinal()] = l.getType();
        }
    }

    public Coordinate getCoordinates() {
        return coord;
    }

    public LinkType getBoundary(Cardinal direction) {
        return boundary[direction.ordinal()];
    }
}
