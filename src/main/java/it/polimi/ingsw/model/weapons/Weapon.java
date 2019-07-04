package it.polimi.ingsw.model.weapons;


import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.Identifiable;
import java.util.ArrayList;
import java.util.List;

public class Weapon extends Identifiable {
    /**
     * The name of the weapon.
     */
    private String name;
    /**
     * Define if the weapon is charged or not.
     */
    private boolean charged = false;
    /**
     * Cost you need to pay when you try to pickup a weapon.
     */
    private List<AmmoColor> pickupColor = new ArrayList<>();
    /**
     * In order to recharge the weapon, you need to pay this cost.
     */
    private AmmoColor additionalRechargeColor;
    /**
     * The list of attacks of the weapon.
     */
    private ArrayList<Attack> attacks = new ArrayList<>();

    public Weapon(){

    }

    /**
     * Constructor of the class.
     * @param name The name of the weapon.
     */
    public Weapon(String name){
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Boolean isCharged() {
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

    /**
     * Get the cost to recharge the weapon.
     * @return The list of ammo to pay to recharge the weapon.
     */
    public List<AmmoColor> getTotalRechargeCost(){
        List<AmmoColor> totalCost = new ArrayList<>(pickupColor);
        if(additionalRechargeColor != null)
            totalCost.add(additionalRechargeColor);

        return totalCost;
    }
}