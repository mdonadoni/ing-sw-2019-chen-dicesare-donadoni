package it.polimi.ingsw.model.weapons;

import com.fasterxml.jackson.databind.JsonNode;
import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.Identifiable;

import java.util.ArrayList;
import java.util.List;

public class Attack extends Identifiable {
    private List<Target> baseFire = new ArrayList<>();
    private List<MovementEffect> bonusMovement = new ArrayList<>();
    private List<AmmoColor> bonusMovementCost = new ArrayList<>();
    private List<Attack> additionalAttacks = new ArrayList<>();
    private List<AmmoColor> cost = new ArrayList<>();
    private String descriptionId;

    public Attack(){ }
    public void addBonusMovement(MovementEffect movement){
        bonusMovement.add(movement);
    }
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
