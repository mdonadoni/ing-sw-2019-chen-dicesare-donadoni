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
    public void addAdditionalAttack(Attack attack){
        additionalAttacks.add(attack);
    }
    public List<Attack> getAdditionalAttacks() {
        return additionalAttacks;
    }
    public void addBaseFire(Target targets){
        baseFire.add(targets);
    }
    public List<Target> getBaseFire(){
        return baseFire;
    }
    public void addCost(AmmoColor ammo){
        cost.add(ammo);
    }
    public List<AmmoColor> getCost(){
        return cost;
    }
    public boolean hasBonusMovement(){
        return !bonusMovement.isEmpty();
    }
    public boolean hasAdditionalAttacks(){
        return !additionalAttacks.isEmpty();
    }
    public void addBonusMovementCost(AmmoColor singleCost){
        bonusMovementCost.add(singleCost);
    }
    public List<AmmoColor> getBonusMovementCost(){
        return new ArrayList<>(bonusMovementCost);
    }
    public void setDescriptionId(String id){
        descriptionId = id;
    }
    public String getDescriptionId(){
        return descriptionId;
    }
}
