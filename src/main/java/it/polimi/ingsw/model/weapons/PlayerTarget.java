package it.polimi.ingsw.model.weapons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.util.ArrayList;
import java.util.List;

public class PlayerTarget extends Target {

    /**
     * Classic auto-init constructor. Sets most of the parameters
     * @param numTargets => numberOfTargets
     * @param vis => visibility
     * @param minD => minDistance
     * @param maxD => maxDistance
     * @param excl => exclusive
     * @param inh => inherited
     */
    @JsonCreator
    public PlayerTarget(@JsonProperty("numberOfTargets") int numTargets,
                        @JsonProperty("visibility") Visibility vis,
                        @JsonProperty("minDistance") int minD,
                        @JsonProperty("maxDistance") int maxD,
                        @JsonProperty("exclusive") boolean excl,
                        @JsonProperty("inherited") boolean inh){
        super(numTargets, vis, minD, maxD, excl, inh);
    }
    PlayerTarget (JsonNode json){
        super(json);
    }
    /**
     * Checks whether a player can be targeted
     * @param attacker The player who is attacking
     * @param target The designed player target
     * @return True if the target can be hit, false otherwise
     */
    public boolean validateTargetPlayer(Player attacker, Player target){
        return validateTargetSquare(attacker, target.getSquare());
    }

    /**
     * Checks whether a group of players can be targeted by an attack
     * @param attacker The player who is attacking
     * @param targets The List of designed targets
     * @return True if it's a legal target list, false otherwise
     */
    public boolean compatibleTargetPlayers(Player attacker, List<Player> targets) {
        List<Square> targetSquares= new ArrayList<>();
        for(Player target : targets)
            targetSquares.add(target.getSquare());
        return compatibleTargetSquares(attacker, targetSquares);
    }
}
