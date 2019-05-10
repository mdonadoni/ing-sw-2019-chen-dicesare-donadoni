package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.BoardType;
import it.polimi.ingsw.model.SpawnPoint;
import it.polimi.ingsw.model.StandardSquare;

import java.io.Serializable;
import java.util.ArrayList;

public class MiniBoard implements Serializable {
    private static final long serialVersionUID = 6047170014618238453L;

    private ArrayList<MiniSpawnPoint> spawnPoints;
    private ArrayList<MiniStandardSquare> standardSquares;
    private BoardType type;

    @JsonCreator
    MiniBoard() {
        spawnPoints = null;
        standardSquares = null;
        type = null;
    }

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

    public ArrayList<MiniSpawnPoint> getSpawnPoints() {
        return new ArrayList<>(spawnPoints);
    }

    public ArrayList<MiniStandardSquare> getStandardSquares() {
        return new ArrayList<>(standardSquares);
    }

    public BoardType getType() {
        return type;
    }
}
