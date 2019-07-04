package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.Identifiable;

/**
 * This class represent the effect of an attack
 */
public class Effect extends Identifiable {
    private int value;

    /**
     * Standard constructor, sets value to a standard value of 0
     */
    public Effect(){
        value = 0;
    }

    /**
     * Constructor that sets 'value'
     * @param value sets value attribute
     */
    public Effect(int value){
        this.value = value;
    }
    /**
     * Get the value of the effect.
     * @return The value of the effect (obvious meaning for Damage, Mark, Movement)
     */
    public int getValue() {
        return value;
    }

    /**
     * Set the value of the effect, how much it is effective.
     * @param value How much 'thing' I have to do (how much damage, how many marks, movement)
     */
    public void setValue(int value) {
        this.value = value;
    }
}
