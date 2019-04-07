package it.polimi.ingsw.model.weapons;

public class Effect {
    private int amount;

    /**
     * Standard constructor, sets amount to a standard value of 0
     */
    public Effect(){
        amount = 0;
    }

    /**
     * Constructor that sets 'amount'
     * @param value sets amount attribute
     */
    public Effect(int value){
        amount = value;
    }
    /**
     * @return The amount of the effect (obvious meaning for Damage, Mark, Movement)
     */
    public int getAmount() {
        return amount;
    }

    /**
     * @param amount How much 'thing' I have to do (how much damage, how many marks, movement)
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
