package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.security.InvalidParameterException;
import java.util.List;

public class SquareTarget extends Target {
    private int numberOfPlayers;
    private int maxPlayerDistance;
    private boolean vortex;

    /**
     * Classic auto-init constructor. Sets most of the parameters
     * @param numTargets => numberOfTargets
     * @param vis => visibility
     * @param minD => minDistance
     * @param maxD => maxDistance
     * @param excl => exclusive
     * @param inh => inherited
     */
    public SquareTarget(int numTargets, Visibility vis, int minD, int maxD, int maxPlayerD, boolean excl, boolean inh){
        super(numTargets, vis, minD, maxD, excl, inh);
        if(maxPlayerDistance < 0)
            throw new InvalidParameterException("maxPlayerDistance must be at least 0");

        maxPlayerDistance = maxPlayerD;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getPlayerMaxDistance() {
        return maxPlayerDistance;
    }

    public void setMaxPlayerDistance(int maxDistance) {
        this.maxPlayerDistance = maxDistance;
    }

    public boolean isVortex() {
        return vortex;
    }

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
