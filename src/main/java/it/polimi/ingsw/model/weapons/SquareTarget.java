package it.polimi.ingsw.model.weapons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * This class represent the
 */
public class SquareTarget extends Target {
    /**
     * Number of player per Square this Target object can hit
     */
    private int numberOfPlayers;
    /**
     * Maximum player distance from any of the squares selected as target
     */
    private int maxPlayerDistance;
    /**
     * The name might be inaccurate, but states whether one of the target squares is fixed for a later effect/attack.
     * When 2 or more squares are selected as target, the nearest one from the attacker is set as fixed, in the
     * FIX_FARTHEST property is set in the Special field, the farthest one is selected to be fixed
     */
    private boolean vortex;

    /**
     * Classic auto-init constructor. Sets most of the parameters
     * @param numTargets =&gt; numberOfTargets
     * @param vis =&gt; visibility
     * @param minD =&gt; minDistance
     * @param maxD =&gt; maxDistance
     * @param excl =&gt; exclusive
     * @param inh =&gt; inherited
     * @param maxPlayerD =&gt; maxPlayerDistance
     * @param numberOfPlayers =&gt; numberOfPlayers
     * @param vortex =&gt; vortex
     */
    @JsonCreator
    public SquareTarget(@JsonProperty("numberOfTargets") int numTargets,
                        @JsonProperty("visibility") Visibility vis,
                        @JsonProperty("minDistance") int minD,
                        @JsonProperty("maxDistance") int maxD,
                        @JsonProperty("exclusive") boolean excl,
                        @JsonProperty("inherited") boolean inh,
                        @JsonProperty("maxPlayerDistance") int maxPlayerD,
                        @JsonProperty("numberOfPlayers") int numberOfPlayers,
                        @JsonProperty("vortex") boolean vortex){
        super(numTargets, vis, minD, maxD, excl, inh);
        if(maxPlayerDistance < 0)
            throw new InvalidParameterException("maxPlayerDistance must be at least 0");

        maxPlayerDistance = maxPlayerD;
        this.numberOfPlayers = numberOfPlayers;
        this.vortex = vortex;
    }

    /**
     * Copying constructor
     * @param target The target to copy
     */
    public SquareTarget(Target target){
        super(target);
    }

    /**
     * Getter for the number of player per Square this Target object can hit
     * @return The number of player per Square this Target object can hit
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * Setter for the number of player per Square this Target object can hit
     * @param numberOfPlayers he number of player per Square this Target object can hit
     */
    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    /**
     * Getter for the maximum player distance from any of the squares selected as target
     * @return The maximum player distance from any of the squares selected as target
     */
    public int getPlayerMaxDistance() {
        return maxPlayerDistance;
    }

    /**
     * Setter for the maximum player distance from any of the squares selected as target
     * @param maxDistance The maximum player distance from any of the squares selected as target
     */
    public void setMaxPlayerDistance(int maxDistance) {
        this.maxPlayerDistance = maxDistance;
    }

    /**
     * Getter for the vortex property
     * @return Whether this target fixes a square
     */
    public boolean isVortex() {
        return vortex;
    }

    /**
     * Setter for the vortex property
     * @param vortex Whether this target fixes a square
     */
    public void setVortex(boolean vortex) {
        this.vortex = vortex;
    }

    /**
     * Checks, given the target square, if a player can be hit by the weapon attack
     * @param attacker The player firing with the weapon
     * @param targetSquare The square chosen as a target
     * @param targetPlayer The player chosen as a target
     * @return  Whether the square can be selected as target of this attack
     */
    public boolean validateTargetPlayer(Player attacker, Square targetSquare, Player targetPlayer){
        boolean result = true;

        // Validate the target square, using the superclass method
        if(!validateTargetSquare(attacker, targetSquare))
            result = false;

        // Verify that the player is within max distance of the target square
        if((targetSquare.getDistance(targetPlayer.getSquare()) > maxPlayerDistance))
            result = false;

        return result;
    }

    /**
     * Checks, given a target square, if a list of players can be hit by the weapon attack
     * @param attacker The attacking player
     * @param targetSquare The square chosen as a target
     * @param targets The List of players chosen as a target
     * @return True if the list of players can be hit, false otherwise
     */
    public boolean compatibleTargetPlayers(Player attacker, Square targetSquare, List<Player> targets) {
        boolean result = true;

        for (Player target : targets){
            if(!validateTargetPlayer(attacker, targetSquare, target))
                result = false;
        }

        return result;
    }

    /**
     * Checks, given a list of target squares, if a list of players can be hit by the weapon attack
     * @param attacker The attacking player
     * @param targetSquares The list of squares chosen as a target
     * @param targets The List of players chosen as a target
     * @return True if the list of players can be hit, false otherwise
     */
    public boolean compatibleTargetPlayers(Player attacker, List<Square> targetSquares, List<Player> targets){
        boolean result = true;

        if (targets.size() > numberOfPlayers)
            result = false;
        if (targetSquares.size() > getNumberOfTargets())
            result = false;

        if(!compatibleTargetSquares(attacker, targetSquares))
            result = false;

        for(Player player : targets){
            boolean isInRange = false;
            for(Square square : targetSquares){
                if(validateTargetPlayer(attacker, square, player))
                    isInRange = true;
            }
            if(!isInRange)
                result = false;
        }
        return result;
    }
}
