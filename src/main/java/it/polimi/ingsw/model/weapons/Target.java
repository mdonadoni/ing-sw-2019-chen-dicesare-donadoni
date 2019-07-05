package it.polimi.ingsw.model.weapons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.Cardinal;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
     * True if the target is fixed by a previous attack or effect
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
     * @param numTargets =&gt; numberOfTargets
     * @param vis =&gt; visibility
     * @param minD =&gt; minDistance
     * @param maxD =&gt; maxDistance
     * @param excl =&gt; exclusive
     * @param inh =&gt; inherited
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

    /**
     * Standard contructor
     */
    public Target(){

    }

    /**
     * Constructor of the class
     * @param target The target to get the setup from.
     */
    public Target(Target target){
        setNumberOfTargets(target.getNumberOfTargets());
        setVisibility(target.getVisibility());
        setMinDistance(target.getMinDistance());
        setMaxDistance(target.getMaxDistance());
        setExclusive(target.isExclusive());
        setInherited(target.isInherited());
        setSpecial(target.getSpecial());
        setEffects(target.getEffects());

    }

    /**
     * Getter for the number of targets that can be selected
     * @return The number of targets that can be selected
     */
    public int getNumberOfTargets() {
        return numberOfTargets;
    }

    /**
     * Setter for the number of targets that can be selected
     * @param numberOfTargets The number of targets that can be selected
     */
    public void setNumberOfTargets(int numberOfTargets) {
        this.numberOfTargets = numberOfTargets;
    }

    /**
     * Getter for the visibility property (VISIBLE, INVISIBLE, DC)
     * @return The visibility property
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * Setter for the visibility property
     * @param visibility The visibility property
     */
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    /**
     * Getter for the minimum distance between the attacker and the targets
     * @return The minimum distance between the attacker and the targets
     */
    public int getMinDistance() {
        return minDistance;
    }

    /**
     * Setter for the minimum distance between the attacker and the targets
     * @param minDistance The minimum distance between the attacker and the targets
     */
    public void setMinDistance(int minDistance) {
        this.minDistance = minDistance;
    }

    /**
     * Getter for the maximum distance between the attacker and the targets
     * @return The maximum distance between the attacker and the targets
     */
    public int getMaxDistance() {
        return maxDistance;
    }

    /**
     * Setter for the maximum distance between the attacker and the targets
     * @param maxDistance The maximum distance between the attacker and the targets
     */
    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    /**
     * Getter for exclusive property
     * @return Whether the target is exclusive
     */
    public boolean isExclusive() {
        return exclusive;
    }

    /**
     * Setter for exclusive property
     * @param exclusive Whether the target is exclusive
     */
    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    /**
     * Getter for the inherited property
     * @return Whether the target is inherited
     */
    public boolean isInherited() {
        return inherited;
    }

    /**
     * Setter for the inherited property
     * @param inherited Whether the target is inherited
     */
    public void setInherited(boolean inherited) {
        this.inherited = inherited;
    }

    /**
     * Setter for the list of special properties, substitutes the current list with a new one
     * @param special The list of special properties to be set
     */
    public void setSpecial(List<SpecialArea> special){
        this.special.clear();
        this.special.addAll(special);
    }

    /**
     * Getter for the list of special properties
     * @return The list of special properties
     */
    public List<SpecialArea> getSpecial(){
        return special;
    }

    /**
     * Unless it's already present, adds a single property to the special list
     * @param special The property to be added
     */
    public void addSpecial(SpecialArea special) {
        if (!this.special.contains(special)) {
            this.special.add(special);
        }
    }

    /**
     * Removes a special property from the list, if present
     * @param special The special property to be removed
     */
    public void removeSpecial(SpecialArea special){
        this.special.remove(special);
    }

    /**
     * @return Whether the targets must be aligned
     */
    public boolean isLine(){
        return special.contains(SpecialArea.LINE);
    }

    /**
     * @return Whether this target hits all the available targets
     */
    public boolean isAOE(){
        return special.contains(SpecialArea.AOE);
    }

    /**
     * @return Whether this target hits also all the other targets in the same room of the selected one
     */
    public boolean isRoom(){
        return special.contains(SpecialArea.ROOM);
    }

    /**
     * @return Whether this target fixes the farthest target selected
     */
    public boolean fixReverse(){
        return special.contains(SpecialArea.FIX_FARTHEST);
    }

    /**
     * @return Whether this target is a chain one, it means that the visibility and distance are calculated based on the
     * position of the last damaged player
     */
    public boolean isChain(){
        return special.contains(SpecialArea.CHAIN);
    }

    /**
     * Adds an Effect object to the effects list
     * @param effect The effect to be added
     */
    public void addEffect(Effect effect){
        effects.add(effect);
    }

    /**
     * Removes an effect, given it's index
     * @param ndx The index of the effect to be removed
     */
    public void removeEffectByIndex(int ndx) {
        effects.remove(ndx);
    }

    /**
     * @return The iterator of the effects list
     */
    public Iterator<Effect> getIterator(){
        return effects.iterator();
    }

    /**
     * Getter for the effects list
     * @return The effects list
     */
    public List<Effect> getEffects(){
        return effects;
    }

    /**
     * Setter for the effects list, substitutes the list with a new one, overriding it
     * @param effects The list to be set
     */
    public void setEffects(List<Effect> effects){
        this.effects.clear();
        this.effects.addAll(effects);
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

        // Can never select my own room
        if(isRoom() && attacker.getSquare().isSameRoom(destSquare))
            result = false;

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
