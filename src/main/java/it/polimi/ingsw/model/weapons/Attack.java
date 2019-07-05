package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.Identifiable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent an attack of a weapon
 */
public class Attack extends Identifiable {
    /**
     * List of target of the attack.
     */
    private List<Target> baseFire = new ArrayList<>();
    /**
     * List of bonus movement effect of the attack.
     */
    private List<MovementEffect> bonusMovement = new ArrayList<>();
    /**
     * List of bonus movement effect cost.
     */
    private List<AmmoColor> bonusMovementCost = new ArrayList<>();
    /**
     * List of additional attacks.
     */
    private List<Attack> additionalAttacks = new ArrayList<>();
    /**
     * List of cost of the attack.
     */
    private List<AmmoColor> cost = new ArrayList<>();
    /**
     * Id of the description of the attack.
     */
    private String descriptionId;

    /**
     * Constructor of the class
     */
    public Attack(){
        // construct empty attack
    }

    /**
     * Add a bonus movement effest.
     * @param movement The bonus movement effect to add.
     */
    public void addBonusMovement(MovementEffect movement){
        bonusMovement.add(movement);
    }

    /**
     * Get bonus movement effect.
     * @return The bonus movement effect.
     */
    public List<MovementEffect> getBonusMovement(){
        return bonusMovement;
    }

    /**
     * Adds an Attack to the list of the additional attacks
     * @param attack The attack to be added
     */
    public void addAdditionalAttack(Attack attack){
        additionalAttacks.add(attack);
    }

    /**
     * Getter for additionalAttacks
     * @return The list of all the additional attacks
     */
    public List<Attack> getAdditionalAttacks() {
        return additionalAttacks;
    }

    /**
     * Adds a Target object to the list of targets
     * @param targets The target to be added
     */
    public void addBaseFire(Target targets){
        baseFire.add(targets);
    }

    /**
     * Getter for the baseFire list
     * @return The list of targets in this attack
     */
    public List<Target> getBaseFire(){
        return baseFire;
    }

    /**
     * Adds a single ammo to the cost of this attack
     * @param ammo The ammo to be added
     */
    public void addCost(AmmoColor ammo){
        cost.add(ammo);
    }

    /**
     * Getter for the cost of the attack
     * @return The cost of the attack
     */
    public List<AmmoColor> getCost(){
        return cost;
    }

    /**
     * States whether the attack has a bonus movement
     * @return Whether bonusMovement list has something in it
     */
    public boolean hasBonusMovement(){
        return !bonusMovement.isEmpty();
    }

    /**
     * States whether the attack has additional attacks
     * @return Whether additionalAttacks list has something in it
     */
    public boolean hasAdditionalAttacks(){
        return !additionalAttacks.isEmpty();
    }

    /**
     * Adds a single ammo to the cost of the bonus movement
     * @param singleCost The ammo to be added
     */
    public void addBonusMovementCost(AmmoColor singleCost){
        bonusMovementCost.add(singleCost);
    }

    /**
     * Getter for the bonus movement cost
     * @return The cost of the bonus movement
     */
    public List<AmmoColor> getBonusMovementCost(){
        return new ArrayList<>(bonusMovementCost);
    }

    /**
     * Setter for the description ID
     * @param id The id to be set
     */
    public void setDescriptionId(String id){
        descriptionId = id;
    }

    /**
     * Getter for the description ID
     * @return The description ID
     */
    public String getDescriptionId(){
        return descriptionId;
    }
}
