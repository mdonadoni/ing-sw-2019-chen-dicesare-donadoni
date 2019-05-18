package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MiniSquare extends Identifiable implements Serializable {
    private static final long serialVersionUID = 2278398549083364767L;

    private final Coordinate coord;
    private final LinkType[] boundary;
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

    public Coordinate getCoordinates() {
        return coord;
    }

    public LinkType getBoundary(Cardinal direction) {
        return boundary[direction.ordinal()];
    }

    public List<PlayerToken> getPlayers() {
        return players;
    }
}
