package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Square for the client.
 */
public class MiniSquare extends MiniIdentifiable implements Serializable {
    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = 2278398549083364767L;
    /**
     * Coordinates of this square.
     */
    private final Coordinate coord;
    /**
     * Boundaries of this square. This boundaries are ordered based on the
     * Cardinal enum.
     */
    private final LinkType[] boundary;
    /**
     * Colors of players inside this square.
     */
    private final ArrayList<PlayerToken> players;

    /**
     * Constructor for jackson.
     */
    @JsonCreator
    MiniSquare() {
        coord = null;
        boundary = null;
        players = null;
    }

    /**
     * Constructor of MiniSquare from a full Square.
     * @param square Square to be copied.
     */
    MiniSquare(Square square) {
        super(square.getUuid());
        coord = square.getCoordinates();
        boundary = new LinkType[4];
        players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            boundary[i] = LinkType.WALL;
        }
        for (Link l : square.getLinks()) {
            boundary[l.getDirectionFromSquare(square).ordinal()] = l.getType();
        }
        square.getPlayers().forEach(p -> players.add(p.getColor()));
    }

    /**
     * Get the coordinates of this square.
     * @return The coordinates of this square.
     */
    public Coordinate getCoordinates() {
        return coord;
    }

    /**
     * Get the boundaries of this square given the direction.
     * @param direction Direction of the boundary.
     * @return The boundary.
     */
    public LinkType getBoundary(Cardinal direction) {
        return boundary[direction.ordinal()];
    }

    /**
     * Get the color of the players inside this square.
     * @return The color of the players in this square.
     */
    public List<PlayerToken> getPlayers() {
        return players;
    }
}
