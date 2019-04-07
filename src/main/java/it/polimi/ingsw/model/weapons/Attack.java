package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.AmmoColor;

import java.util.ArrayList;
import java.util.List;

public class Attack {
    private Boolean chainAttack;
    private List<Target> baseFire;
    private MovementEffect bonusMovement;
    private List<Attack> additionalAttacks;
    private List<AmmoColor> cost;

    Attack(){
        additionalAttacks = new ArrayList<>();
        baseFire = new ArrayList<>();
        cost = new ArrayList<>();
    }
    public void setBonusMovement(MovementEffect movement){
        bonusMovement = movement;
    }
    public MovementEffect getBonusMovement(){
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
    public void setChainAttack(Boolean chainAttack) {
        this.chainAttack = chainAttack;
    }
    public boolean getChainAttack(){
        return chainAttack;
    }
    public void addCost(AmmoColor ammo){
        cost.add(ammo);
    }
    public List<AmmoColor> getCost(){
        return cost;
    }
}
