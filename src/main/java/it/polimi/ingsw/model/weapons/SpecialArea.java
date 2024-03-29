package it.polimi.ingsw.model.weapons;

/**
 * Enumeration of the special area of a target.
 */
public enum SpecialArea {
    /**
     * The targets are in a line.
     */
    LINE,
    /**
     * The targets are all the targets available.
     */
    AOE,
    /**
     * The target is a player in the current room.
     */
    ROOM,
    /**
     * If the attack fixes a target, in case of multiple target, the farthest one will be selected. Should always be
     * used with LINE.
     */
    FIX_FARTHEST,
    /**
     * The visibility of the target must be evaluated using the last target hit.
     */
    CHAIN
}
