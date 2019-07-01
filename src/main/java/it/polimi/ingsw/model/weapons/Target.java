package it.polimi.ingsw.model.weapons;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import it.polimi.ingsw.model.*;

public class Target{

    /**
     * Number of targets to be selected
     */
    private int numberOfTargets;
    /**
     * States whether the target must be visible to the attacker
     */
    private Visibility visibility;
    /**
     * Minimum distance between attacker and target.
     * If both minimum and maximum distance are set to -1, the distance doesn't matter.
     */
    private int minDistance;
    /**
     * Maximum distance between attacker and target
     * If both minimum and maximum distance are set to -1, the distance doesn't matter.
     */
    private int maxDistance;
    /**
     * True if the target mustn't be the target of another effect of this weapon, False otherwise
     */
    private boolean exclusive;
    /**
     * True if the target is fixed by the previous attack
     */
    private boolean inherited;
    /**
     * List of special area effects
     */
    private ArrayList<SpecialArea> special = new ArrayList<>();
    /**
     * List of effects dealt to the targets
     */
    private ArrayList<Effect> effects = new ArrayList<>();

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
    public Target(@JsonProperty("numberOfTargets") int numTargets,
                  @JsonProperty("visibility") Visibility vis,
                  @JsonProperty("minDistance") int minD,
                  @JsonProperty("maxDistance") int maxD,
                  @JsonProperty("exclusive") boolean excl,
                  @JsonProperty("inherited") boolean inh){
        if (numTargets<1)
            throw new InvalidParameterException("Number of targets must be always at least 1");
        if (minD < -1 || maxD < -1)
            throw new InvalidParameterException("minD and maxD cannot be lower than -1");
        if (minD > maxD && maxD > -1)
            throw new InvalidParameterException("minD must be lower than maxD");

        numberOfTargets = numTargets;
        visibility = vis;
        minDistance = minD;
        maxDistance = maxD;
        exclusive = excl;
        inherited = inh;
    }
    Target(JsonNode json){
        setNumberOfTargets(json.get("numberOfTargets").asInt());
        setVisibility(Visibility.valueOf(json.get("visibility").asText().toUpperCase()));
        setMinDistance(json.get("minDistance").asInt());
        setMaxDistance(json.get("maxDistance").asInt());
        setExclusive(json.get("exclusive").asBoolean());
        setInherited(json.get("inherited").asBoolean());
        for(JsonNode special : json.get("special"))
        {
            addSpecial(SpecialArea.valueOf(special.asText()));
        }
        for(JsonNode effect : json.get("effects")){
            if(effect.get("type").asText().equals("harmful"))
                addEffect(new HarmfulEffect(effect));
            else if(effect.get("type").asText().equals("movement"))
                addEffect(new MovementEffect(effect));
        }
    }
    public int getNumberOfTargets() {
        return numberOfTargets;
    }
    public void setNumberOfTargets(int numberOfPlayers) {
        this.numberOfTargets = numberOfPlayers;
    }
    public Visibility getVisibility() {
        return visibility;
    }
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
    public int getMinDistance() {
        return minDistance;
    }
    public void setMinDistance(int minDistance) {
        this.minDistance = minDistance;
    }
    public int getMaxDistance() {
        return maxDistance;
    }
    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }
    public boolean isExclusive() {
        return exclusive;
    }
    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }
    public boolean isInherited() {
        return inherited;
    }
    public void setInherited(boolean inherited) {
        this.inherited = inherited;
    }
    public List<SpecialArea> getSpecial(){
        return special;
    }
    public void addSpecial(SpecialArea special) {
        if (!this.special.contains(special)) {
            this.special.add(special);
        }
    }
    public void removeSpecial(SpecialArea special){
        this.special.remove(special);
    }
    public boolean isLine(){
        return special.contains(SpecialArea.LINE);
    }
    public boolean isAOE(){
        return special.contains(SpecialArea.AOE);
    }
    public boolean isRoom(){
        return special.contains(SpecialArea.ROOM);
    }
    public boolean fixReverse(){
        return special.contains(SpecialArea.FIX_FARTHEST);
    }
    public void addEffect(Effect effect){
        effects.add(effect);
    }
    public void removeEffectByIndex(int ndx) {
        effects.remove(ndx);
    }
    public Iterator<Effect> getIterator(){
        return effects.iterator();
    }
    public List<Effect> getEffects(){
        return effects;
    }

    /**
     * This method is used to check whether the target square can be targeted from the origin square, based only on
     * the visibility.
     * @param origin The square of the attacker
     * @param target The square of which I want to check the visibility
     * @return True if the visibility constraint is compatible with the visibility of the target square. False otherwise
     */
    boolean checkSquareVisibility(Square origin, Square target){
        boolean result;
        switch(getVisibility()){
            case VISIBLE:
                result = origin.isVisible(target);
                return result;
            case INVISIBLE:
                result = !origin.isVisible(target);
                return result;
            case DC:
                return true;
            default:
                // The default branch won't be triggered in any situation since getVisibility() returns nothing but
                // VISIBLE, INVISIBLE or DC
                return false;
        }
    }
    /**
     * This method is used to check whether the target square can be targeted from the origin square, based only on
     * the distance
     * @param origin The square of the attacker
     * @param target The square of which I want to check the distance
     * @return True if the distance constraint is satisfied
     */
    boolean checkDistance(Square origin, Square target){
        boolean minDistOk = true;
        boolean maxDistOk = true;
        int distance = origin.getDistance(target);

        if(minDistance!=-1 && minDistance > distance)
            minDistOk = false;

        if(maxDistance!=-1 && maxDistance < distance)
            maxDistOk = false;

        return (minDistOk && maxDistOk);
    }
    /**
     * This method checks whether the target square can be a valid one
     * @param attacker The player firing with the weapon
     * @param destSquare The target chose for the attack
     * @return Whether the target can be hit by the attacker with this specific weapon attack
     */
    public boolean validateTargetSquare(Player attacker, Square destSquare){
        boolean result = true;
        Square originSquare = attacker.getSquare();

        // Validate target based on the visibility
        if (!checkSquareVisibility(originSquare, destSquare))
            result = false;

        // Validate target based on the distance
        if (!checkDistance(originSquare, destSquare))
            result = false;

        // Validate target based on the 'line' property
        if (isLine()) {
            boolean somehowAligned = false;
            for (Cardinal direction : Cardinal.values())
                // Check if the target square is aligned in a certain direction
                if (originSquare.isAligned(direction, destSquare))
                    somehowAligned = true;
            if (!somehowAligned)
                result = false;
        }

        return result;
    }
    /**
     * This method checks whether a list of target squares can be targeted as a group
     * @param attacker The player firing with the weapon
     * @param targets A list containing some squares chose as targets
     * @return Whether the targets, as a group, can be selected for the attack
     */
    public boolean compatibleTargetSquares(Player attacker, List<Square>targets){
        boolean result = true;
        Square originSquare = attacker.getSquare();

        // Stay humble
        if(numberOfTargets < targets.size())
            result = false;

        // Check whether the players can be targeted
        for (Square target : targets) {
            if (!validateTargetSquare(attacker, target))
                result = false;
        }

        // If the line property is present, I need to verify that all the targets are aligned in a certain direction
        if (isLine()) {
            boolean superAligned = false;
            // First I cycle through all the possible cardinal directions
            for (Cardinal direction : Cardinal.values()){
                boolean aligned = true;
                List<Square> squares = originSquare.getAlignedSquares(direction);
                // Checking whether all the targets are in the chosen direction
                for (Square target : targets){
                    if(!squares.contains(target))
                        aligned = false; // I need ALL the targets to be in the same direction, go big or go home
                }
                superAligned = superAligned || aligned; // I just need one direction to be verified (99.99% of cases)
            }
            result = superAligned;
        }
        return result;
    }
}
