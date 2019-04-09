package it.polimi.ingsw.model.weapons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.AmmoColor;

import java.util.ArrayList;
import java.util.List;

public class Weapon {
    private String name;
    private Boolean charged;
    private List<AmmoColor> pickupColor;
    private AmmoColor additionalRechargeColor;
    private ArrayList<Attack> attacks = new ArrayList<>();
    @JsonCreator
    public Weapon(@JsonProperty("name") String name){
        this.name = name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public Boolean getCharged() {
        return charged;
    }
    public void setCharged(Boolean charged) {
        this.charged = charged;
    }
    public List<AmmoColor> getPickupColor() {
        return pickupColor;
    }
    public void addPickupColor(AmmoColor ammo) {
        pickupColor.add(ammo);
    }
    public AmmoColor getAdditionalRechargeColor() {
        return additionalRechargeColor;
    }
    public void setAdditionalRechargeColor(AmmoColor additionalRechargeColor) {
        this.additionalRechargeColor = additionalRechargeColor;
    }
    public List<Attack> getAttacks(){
        return attacks;
    }
    public void addAttack(Attack attack){
        attacks.add(attack);
    }
    public void setAttack(int ndx, Attack attack){
        if (attacks.size() > ndx){
            attacks.set(ndx, attack);
        }
    }
    public void removeAttack(int ndx){
        attacks.remove(ndx);
    }

}