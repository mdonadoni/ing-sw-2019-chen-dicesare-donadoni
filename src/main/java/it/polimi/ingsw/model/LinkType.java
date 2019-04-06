package it.polimi.ingsw.model;

/**
 * Enum to describe the type of a Link.
 * A Link can have one of three types:
 *  - DOOR: link between two squares connected by a door
 *  - WALL: link between two squares that share a wall
 *  - SAME_ROOM: link between two squares that are in the same room
 */
public enum LinkType {
    DOOR,
    WALL,
    SAME_ROOM,
}
