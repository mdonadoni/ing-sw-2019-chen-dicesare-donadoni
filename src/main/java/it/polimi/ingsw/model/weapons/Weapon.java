package it.polimi.ingsw.model.weapons;


import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.Identifiable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Weapon of the game
 */
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

    /**
     * Standard constructor
     */
    public Weapon(){

    }

    /**
     * Constructor of the class.
     * @param name The name of the weapon.
     */
    public Weapon(String name){
        this.name = name;
    }

    /**
     * Setter for the name of the weapon
     * @param name The name of the weapon
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the name of the weapon
     * @return The name of the weapon
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the charged property
     * @return Whether the weapon is charged
     */
    public Boolean isCharged() {
        return charged;
    }

    /**
     * Setter for the charged property
     * @param charged Whether the weapon is charged
     */
    public void setCharged(Boolean charged) {
        this.charged = charged;
    }

    /**
     * Getter for the pickup cost, in ammo
     * @return The pickup cost
     */
    public List<AmmoColor> getPickupColor() {
        return pickupColor;
    }

    /**
     * Adds a single ammo to the pickup cost of the weapon
     * @param ammo The ammo to be added to the cost
     */
    public void addPickupColor(AmmoColor ammo) {
        pickupColor.add(ammo);
    }

    /**
     * Getter for the additional recharge cost
     * @return The color of the additional ammo requested to recharge this weapon
     */
    public AmmoColor getAdditionalRechargeColor() {
        return additionalRechargeColor;
    }

    /**
     * Setter for the additional recharge cost
     * @param additionalRechargeColor The ammo color to be set
     */
    public void setAdditionalRechargeColor(AmmoColor additionalRechargeColor) {
        this.additionalRechargeColor = additionalRechargeColor;
    }

    /**
     * Getter for the attacks list
     * @return The list of the attacks
     */
    public List<Attack> getAttacks(){
        return attacks;
    }

    /**
     * Adds an attack to the list
     * @param attack The attack to be added
     */
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