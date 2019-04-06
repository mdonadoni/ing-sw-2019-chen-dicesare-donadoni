package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Predicate;

/**
 * This class represents a single square of the board.
 */
public class Square {
    /**
     * Coordinate of the square. Identifies the square inside a GameBoard.
     */
    private final Coordinate coord;

    /**
     * List of players that are inside this square.
     */
    private List<Player> players = new ArrayList<>();

    /**
     * List of links between this square and the other squares of the GameBoard.
     */
    private List<Link> links = new ArrayList<>();

    /**
     * Constructor of Square.
     * @param coord coordinates of the square.
     */
    public Square(Coordinate coord) {
        this.coord = coord;
    }

    /**
     * Get square's coordinates.
     * @return Coordinates of square.
     */
    public Coordinate getCoordinates() {
        return coord;
    }

    /**
     * Add a player inside this square.
     * @param player Player to be added.
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Removes a player from this square.
     * @param player Player to be removed.
     */
    public void removePlayer(Player player) {
        players.remove(player);
    }

    /**
     * Get the list of player in this square.
     * @return List of players.
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * Add a link that connects this square with another square.
     * @param link Link to be added.
     */
    public void addLink(Link link) {
        if (!link.getFirstSquare().equals(this) && !link.getSecondSquare().equals(this)) {
            throw new InvalidLinkException("Square not part of the Link");
        }
        for (Link l2 : links) {
            if (link.getDirectionFromSquare(this) == l2.getDirectionFromSquare(this)) {
                throw new InvalidLinkException("Link with same direction already present");
            }
        }
        links.add(link);
    }

    /**
     * Make a BFS from this square and return the distance of all other squares.
     * @param isValidLink Predicate to filter links that do not need to be used
     *                    during the BFS.
     * @return Every square reachable is mapped to its distance from this square.
     *
     */
    private Map<Square, Integer> bfs(Predicate<Link> isValidLink) {
        // maps square to distance from origin of bfs
        Map<Square, Integer> distance = new HashMap<>();
        distance.put(this, 0);
        // dfs stack
        Queue<Square> queue = new LinkedList<>();
        queue.add(this);

        final int INF = 1000000;

        while (!queue.isEmpty()) {
            // get square
            Square front = queue.remove();
            int frontDistance = distance.get(front);

            for (Link l : front.links) {
                if (isValidLink.test(l)) {
                    Square other = l.getOtherSquare(front);

                    int otherDistance = INF;
                    if (distance.containsKey(other)) {
                        otherDistance = distance.get(other);
                    }

                    // check if other square's distance can be improved
                    if (frontDistance + 1 < otherDistance) {
                        distance.put(other, frontDistance+1);
                        queue.add(other);
                    }
                }
            }
        }
        return distance;
    }

    /**
     * Get distance between this and other square.
     * @param other Other square.
     * @return Distance between this square and other square.
     */
    public int getDistance(Square other) {
        Map<Square, Integer> dist = bfs((Link l) -> !l.isWall());
        return dist.get(other);
    }

    /**
     * Get the list of squares that are in the same room of this square,
     * including this square.
     * @return List of squares in the same room.
     */
    public List<Square> getRoomSquares() {
        Map<Square, Integer> dist = bfs(Link::isSameRoom);
        return new ArrayList<>(dist.keySet());
    }

    /**
     * Given a direction, returns the list of squares that are aligned in that direction.
     * @param direction Direction of the alignment.
     * @return List of square aligned along given direction (this square included).
     */
    public List<Square> getAlignedSquares(Cardinal direction) {
        List<Square> squares = new ArrayList<>();
        squares.add(this);
        boolean newSquareAdded = true;
        while (newSquareAdded) {
            newSquareAdded = false;
            Square last = squares.get(squares.size()-1);
            for (Link l : last.links) {
                if (l.getDirectionFromSquare(last) == direction) {
                    newSquareAdded = true;
                    squares.add(l.getOtherSquare(last));
                    break;
                }
            }
        }
        return squares;
    }

    /**
     * Get list of squares visible from this square, this square included.
     * @return List of squares visible.
     */
    public List<Square> getVisibleSquares() {
        List<Square> squares = getRoomSquares();
        for (Link l : links) {
            if (l.isDoor()) {
                squares.addAll(l.getOtherSquare(this).getRoomSquares());
            }
        }
        return squares;
    }

    /**
     * Check if other square is in the same room of this square.
     * @param other Other square to be checked.
     * @return True if squares are in the same room, otherwise false.
     */
    public boolean isSameRoom(Square other) {
        return getRoomSquares().contains(other);
    }

    /**
     * Check if other square is visible from this square.
     * @param other Other square to be checked.
     * @return True if other square is visible, otherwise false.
     */
    public boolean isVisible(Square other) {
        return getVisibleSquares().contains(other);
    }
}
