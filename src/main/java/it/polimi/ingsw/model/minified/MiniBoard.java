package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.BoardType;
import it.polimi.ingsw.model.SpawnPoint;
import it.polimi.ingsw.model.StandardSquare;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Board for the client side.
 */
public class MiniBoard implements Serializable {
    /**
     * Serializable UID
     */
    private static final long serialVersionUID = 6047170014618238453L;
    /**
     * List of spawn points
     */
    private ArrayList<MiniSpawnPoint> spawnPoints;
    /**
     * List of standard squares
     */
    private ArrayList<MiniStandardSquare> standardSquares;
    /**
     * Type of board
     */
    private BoardType type;

    @JsonCreator
    private MiniBoard() {
        spawnPoints = null;
        standardSquares = null;
        type = null;
    }

    /**
     * Constructor of the class
     * @param board The Board to convert
     */
    MiniBoard(Board board) {
        type = board.getType();

        spawnPoints = new ArrayList<>();
        for (SpawnPoint spawn : board.getSpawnPoints()) {
            spawnPoints.add(new MiniSpawnPoint(spawn));
        }

        standardSquares = new ArrayList<>();
        for (StandardSquare std : board.getStandardSquares()) {
            standardSquares.add(new MiniStandardSquare(std));
        }
    }

    public List<MiniSpawnPoint> getSpawnPoints() {
        return new ArrayList<>(spawnPoints);
    }

    public List<MiniStandardSquare> getStandardSquares() {
        return new ArrayList<>(standardSquares);
    }

    public BoardType getType() {
        return type;
    }
}
