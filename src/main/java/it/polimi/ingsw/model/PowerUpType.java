package it.polimi.ingsw.model;

/**
 * Enumeration of the types of power-up
 * Targeting scope: when you are dealing damage to one or more targets. Pay 1 ammo cube of any color.
 *                  Choose 1 of those targets and give it an extra point of damage.
 * Newton: You may play this card on your turn before or after any action.
 *         Choose any other player's figure and move it 1 or 2 squares in one direction
 * Tagback granade: You may play this card when you receive damage from a player you can see. Give that player 1 mark.
 * Teleporter: You may play this card on your turn before or after any action. Pick up your figure and set it down
 *             on any square of the board.
 */
public enum PowerUpType {
    TARGETING_SCOPE,
    NEWTON,
    TAGBACK_GRANADE,
    TELEPORTER
}
