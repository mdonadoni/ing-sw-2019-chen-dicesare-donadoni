package it.polimi.ingsw.model.weapons;

public class HarmfulEffect extends Effect {
    private HarmType type;

    /**
     * @param value number to be assigned to value attribute
     * @param type Mark or Damage?
     */
    public HarmfulEffect(int value, HarmType type){
        setAmount(value);
        this.type = type;
    }
    /**
     * @return The type of the harm inflicted: DAMAGE or MARK
     */
    public HarmType getType() {
        return type;
    }

    /**
     * @param type The type of the harm inflicted. Must be DAMAGE or MARK
     */
    public void setType(HarmType type) {
        this.type = type;
    }
}
