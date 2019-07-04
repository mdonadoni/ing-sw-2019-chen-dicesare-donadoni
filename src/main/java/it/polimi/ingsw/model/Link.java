package it.polimi.ingsw.model;

/**
 * This class represents a link between two adjacent squares on the GameBoard.
 */
public class Link {
    /**
     * First endpoint of the link.
     */
    private Square firstSquare;

    /**
     * Second endpoint of the link.
     */
    private Square secondSquare;
    /**
     * Type of link.
     */
    private LinkType type;

    /**
     * Constructor of a link between two squares.
     * @param firstSquare First endpoint of the link.
     * @param secondSquare Second endpoint of the link.
     * @param type Type of link.
     */
    public Link(Square firstSquare, Square secondSquare, LinkType type) {
        Coordinate coordA = firstSquare.getCoordinates();
        Coordinate coordB = secondSquare.getCoordinates();
        if (coordA.equals(coordB)) {
            throw new InvalidSquareException("Squares can't have same coordinates");
        }
        if (coordA.getRow() != coordB.getRow() &&
                coordA.getColumn() != coordB.getColumn()) {
            throw new InvalidSquareException("Squares must be adjacent");
        }
        this.firstSquare = firstSquare;
        this.secondSquare = secondSquare;
        this.type = type;
    }

    /**
     * Check if this link connects two squares that share a wall.
     * @return True if link has type "WALL", otherwise false.
     */
    public boolean isWall() {
        return type == LinkType.WALL;
    }

    /**
     * Check if this link connects two squares that share a door.
     * @return True if link has type "DOOR", otherwise false.
     */
    public boolean isDoor() {
        return type == LinkType.DOOR;
    }

    /**
     * Check if this link connects two squares in the same room.
     * @return True if link has type "SAME_ROOM", otherwise false.
     */
    public boolean isSameRoom() {
        return type == LinkType.SAME_ROOM;
    }

    /**
     * Get the type of the link.
     * @return The type of the link.
     */
    public LinkType getType() {
        return type;
    }

    /**
     * Get the first endpoint of this link.
     * @return First endpoint of this link.
     */
    public Square getFirstSquare() {
        return firstSquare;
    }

    /**
     * Get the second endpoint of this link.
     * @return Second endpoint of this link.
     */
    public Square getSecondSquare() {
        return secondSquare;
    }

    /**
     * Given an "origin" square, return the other square in this link.
     * @param origin Square in this link.
     * @return Returns the other square in this link.
     */
    public Square getOtherSquare(Square origin) {
        if (origin.equals(firstSquare)) {
            return secondSquare;
        } else if (origin.equals(secondSquare)){
            return firstSquare;
        } else {
            throw new InvalidSquareException("Square is not part of the link");
        }
    }

    /**
     * Given an "origin" square, return the direction of this link starting
     * from the origin and going to the other square.
     * @param origin Square to be used to calculate the direction.
     * @return The direction of the link
     */
    public Cardinal getDirectionFromSquare(Square origin) {
        Coordinate coordFirst;
        Coordinate coordSecond;

        if (origin.equals(firstSquare)) {
            coordFirst = firstSquare.getCoordinates();
            coordSecond = secondSquare.getCoordinates();
        }
        else if (origin.equals(secondSquare)) {
            coordFirst = secondSquare.getCoordinates();
            coordSecond = firstSquare.getCoordinates();
        } else {
            throw new InvalidSquareException("Square is not part of the link");
        }

        if (coordFirst.getColumn() == coordSecond.getColumn()) {
            if (coordFirst.getRow()+1 == coordSecond.getRow()) {
                return Cardinal.SOUTH;
            } else {
                return Cardinal.NORTH;
            }
        } else {
            if (coordFirst.getColumn()+1 == coordSecond.getColumn()) {
                return Cardinal.EAST;
            } else {
                return Cardinal.WEST;
            }
        }
    }
}
