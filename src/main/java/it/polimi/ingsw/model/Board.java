package it.polimi.ingsw.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.*;
import java.util.function.Function;

/**
 * This class represents a board of the game.
 */
public class Board {
    /**
     * Maps coordinate to corresponding square.
     */
    private Map<Coordinate, Square> coordToSquare;

    /**
     * Maps coordinate to corresponding SpawnPoint.
     */
    private Map<Coordinate, SpawnPoint> coordToSpawnPoint;

    /**
     * Maps coordinate do corresponding StandardSquare.
     */
    private Map<Coordinate, StandardSquare> coordToStandardSquare;

    /**
     * Maps color to corresponding SpawnPoint.
     */
    private EnumMap<AmmoColor, SpawnPoint> colorToSpawnPoint;

    /**
     * Create new board given board's type, reading from resources.
     * @param boardType Type of board.
     * @throws ResourceException If there are errors reading or parsing the resource file.
     */
    public Board(BoardType boardType) throws ResourceException {
        this();

        try {
            String path = "/boards/" + boardType.name().toLowerCase() + ".json";
            InputStream in = Board.class.getResourceAsStream(path);
            ObjectMapper mapper = Json.getMapper();
            JsonNode json = mapper.readTree(in);

            Function<JsonNode, Coordinate> jsonToCoord = obj ->
                    new Coordinate(
                            obj.get("row").asInt(),
                            obj.get("column").asInt()
                    );

            for (JsonNode standard : json.get("standard")) {
                addStandardSquare(jsonToCoord.apply(standard));
            }

            for (JsonNode spawn : json.get("spawnpoint")) {
                AmmoColor color = AmmoColor.valueOf(spawn.get("color").asText().toUpperCase());
                addSpawnPoint(jsonToCoord.apply(spawn), color);
            }

            for (JsonNode link : json.get("links")) {
                Coordinate first = jsonToCoord.apply(link.get("first"));
                Coordinate second = jsonToCoord.apply(link.get("second"));
                LinkType linkType = LinkType.valueOf(link.get("type").asText().toUpperCase());
                addLink(first, second, linkType);
            }
        } catch (Exception e) {
            throw new ResourceException("Cannot load board json resource", e);
        }
    }

    /**
     * Constructor of an empty Board.
     */
    public Board() {
        coordToSquare = new HashMap<>();
        coordToSpawnPoint = new HashMap<>();
        coordToStandardSquare = new HashMap<>();
        colorToSpawnPoint = new EnumMap<>(AmmoColor.class);
    }

    /**
     * Add square to coordToSquare, checking some constraints. This method is private.
     * @param sq Square to be added.
     */
    private void addSquare(Square sq) {
        if (coordToSquare.containsKey(sq.getCoordinates())) {
            throw new InvalidOperationException("Cannot have two squares with same coordinates");
        }
        coordToSquare.put(sq.getCoordinates(), sq);
    }

    /**
     * Add new SpawnPoint to the board.
     * @param coord Coordinates of the SpawnPoint.
     * @param color Color of the SpawnPoint.
     */
    public void addSpawnPoint(Coordinate coord, AmmoColor color) {
        if (colorToSpawnPoint.containsKey(color)) {
            throw new InvalidOperationException("Cannot have two SpawnPoint with same color");
        }
        SpawnPoint spawn = new SpawnPoint(coord, color);
        addSquare(spawn);
        colorToSpawnPoint.put(color, spawn);
        coordToSpawnPoint.put(coord, spawn);
    }

    /**
     * Add new StandardSquare to the board.
     * @param coord Coordinates of the StandardSquare.
     */
    public void addStandardSquare(Coordinate coord) {
        StandardSquare std = new StandardSquare(coord);
        addSquare(std);
        coordToStandardSquare.put(coord, std);
    }

    /**
     * Add link between two square of the board.
     * @param firstSquare First square of the link.
     * @param secondSquare Second square of the link.
     * @param type Type of the link.
     */
    public void addLink(Square firstSquare, Square secondSquare, LinkType type) {
        if (!coordToSquare.get(firstSquare.getCoordinates()).equals(firstSquare)) {
            throw new InvalidSquareException("First square is not a valid square");
        }
        if (!coordToSquare.get(secondSquare.getCoordinates()).equals(secondSquare)) {
            throw new InvalidSquareException("Second square is not a valid square");
        }
        Link l = new Link(firstSquare, secondSquare, type);
        firstSquare.addLink(l);
        secondSquare.addLink(l);
    }

    /**
     * Add link between two squares, given their coordinates.
     * @param firstCoordinate Coordinates of first square.
     * @param secondCoordinate Coordinates of second square.
     * @param type Type of the link.
     */
    public void addLink(Coordinate firstCoordinate, Coordinate secondCoordinate, LinkType type) {
        if (!coordToSquare.containsKey(firstCoordinate)) {
            throw new InvalidSquareException("First coordinate is not a valid coordinate");
        }
        if (!coordToSquare.containsKey(secondCoordinate)) {
            throw new InvalidSquareException("Second coordinate is not a valid coordinate");
        }
        addLink(coordToSquare.get(firstCoordinate),
                coordToSquare.get(secondCoordinate),
                type);
    }

    /**
     * Get square of the board.
     * @param coord Coordinates of the square.
     * @return Square with given coordinates.
     */
    public Square getSquare(Coordinate coord) {
        if (!coordToSquare.containsKey(coord)) {
            throw new InvalidOperationException("Cannot find square with given coordinates");
        }
        return coordToSquare.get(coord);
    }

    /**
     * Check if given coordinates correspond to a SpawnPoint.
     * @param coord Coordinates of the square to be checked.
     * @return True if square is a SpawnPoint, else false.
     */
    public boolean isSpawnPoint(Coordinate coord) {
        return coordToSpawnPoint.containsKey(coord);
    }

    /**
     * Check if given coordinates correspond to StandardSquare.
     * @param coord Coordinates of the square to be checked.
     * @return True if the square is a StandardSquare, else false.
     */
    public boolean isStandardSquare(Coordinate coord) {
        return coordToStandardSquare.containsKey(coord);
    }

    /**
     * Get SpawnPoint given coordinates.
     * @param coord Coordinates of the SpawnPoint.
     * @return SpawnPoint with given coordinates.
     */
    public SpawnPoint getSpawnPoint(Coordinate coord) {
        if (!coordToSpawnPoint.containsKey(coord)) {
            throw new InvalidOperationException("Cannot find spawnpoint with given coordinates");
        }
        return coordToSpawnPoint.get(coord);
    }

    /**
     * Get StandardSqure given coordinates.
     * @param coord Coordinates of the StandardSquare.
     * @return StandardSquare with given coordinates.
     */
    public StandardSquare getStandardSquare(Coordinate coord) {
        if (!coordToStandardSquare.containsKey(coord)) {
            throw new InvalidOperationException("Cannot find spawnpoint with given coordinates");
        }
        return coordToStandardSquare.get(coord);
    }

    /**
     * Get SpawnPoint given color.
     * @param color Color of the SpawnPoint.
     * @return SpawnPoint with given color.
     */
    public SpawnPoint getSpawnPointByColor(AmmoColor color) {
        if (!colorToSpawnPoint.containsKey(color)) {
            throw new InvalidOperationException("Cannot find spawnpoint with given color");
        }
        return colorToSpawnPoint.get(color);
    }
}
