package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkTest {

    Square S00, S01, S10;

    @BeforeEach
    void setUp() {
        S00 = new Square(new Coordinate(0,0));
        S01 = new Square(new Coordinate(0,1));
        S10 = new Square(new Coordinate(1,0));
    }

    @Test
    void constructorThrowSameSquare() {
        assertThrows(InvalidSquareException.class,
                () ->  new Link(S00, S00, LinkType.DOOR));
    }

    @Test
    void constructorThrowNonAdjacentSquares() {
        assertThrows(InvalidSquareException.class,
                () -> new Link(S10, S01, LinkType.SAME_ROOM));
    }

    @Test
    void getOtherSquare() {
        Link l = new Link(S00, S01, LinkType.WALL);
        assertEquals(l.getOtherSquare(S00), S01);
        assertEquals(l.getOtherSquare(S01), S00);
    }

    @Test
    void getOtherSquareThrowSquareNotPartOfLink() {
        Link l = new Link(S00, S10, LinkType.DOOR);
        assertThrows(InvalidSquareException.class,
                () ->  l.getOtherSquare(S01));
    }

    @Test
    void getDirectionFromSquareEastWest() {
        Link l = new Link(S00, S01, LinkType.WALL);
        assertEquals(l.getDirectionFromSquare(S00), Cardinal.EAST);
        assertEquals(l.getDirectionFromSquare(S01), Cardinal.WEST);
    }

    @Test
    void getDirectionFromSquareSouthNorth() {
        Link l = new Link(S00, S10, LinkType.WALL);
        assertEquals(l.getDirectionFromSquare(S00), Cardinal.SOUTH);
        assertEquals(l.getDirectionFromSquare(S10), Cardinal.NORTH);
    }

    @Test
    void getDirectionFromSquareThrowSquareNotPartOfLink() {
        Link l = new Link(S00, S10, LinkType.DOOR);
        assertThrows(InvalidSquareException.class,
                () -> l.getDirectionFromSquare(S01));
    }
}