package it.polimi.ingsw.model;

import it.polimi.ingsw.model.weapons.Weapon;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a SpawnPoint. A SpawnPoint is a square which can hold
 * up to three weapons.
 */
public class SpawnPoint extends Square {
    /**
     * The maximum amount of weapon in a spawn point.
     */
    private static final int MAX_WEAPON_SPAWNPOINT = 3;
    /**
     * Color of the SpawnPoint.
     */
    private AmmoColor color;

    /**
     * List of weapons in the SpawnPoint.
     */
    private List<Weapon> weapons = new ArrayList<>();


    /**
     * Constructor of a SpawnPoint.
     * @param coord Coordinates of the SpawnPoint.
     * @param color Color of the SpawnPoint.
     */
    public SpawnPoint(Coordinate coord, AmmoColor color) {
        super(coord);
        this.color = color;
    }

    /**
     * Get the color of the SpawnPoint.
     * @return Color of the SpawnPoint.
     */
    public AmmoColor getColor() {
        return color;
    }

    /**
     * Get the list of available weapons.
     * @return List of available weapons.
     */
    public List<Weapon> getWeapons() {
        return new ArrayList<>(weapons);
    }

    /**
     * Remove a weapon from this SpawnPoint.
     * @param weapon Weapon to be removed.
     */
    public void removeWeapon(Weapon weapon) {
        if (!weapons.remove(weapon)) {
            throw new InvalidOperationException("Weapon not found");
        }
    }

    /**
     * Remove all the weapons from the spawn point.
     */
    public void removeAllWeapons(){
        weapons.clear();
    }

    /**
     * Add a weapon to this SpawnPoint.
     * @param weapon Weapon to be added.
     */
    public void addWeapon(Weapon weapon) {
        if (weapons.size() == MAX_WEAPON_SPAWNPOINT) {
            throw new InvalidOperationException("Too many weapons");
        }
        weapons.add(weapon);
    }

    /**
     * Return if the spawn point is full or not.
     * @return True the spawn point is full, false otherwise.
     */
    public boolean isFull() {
        return weapons.size() == MAX_WEAPON_SPAWNPOINT;
    }

    /**
     * Get the number of Weapons.
     * @return Number of weapons available.
     */
    public int countWeapons() {
        return weapons.size();
    }
}
