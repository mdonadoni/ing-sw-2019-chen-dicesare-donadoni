package it.polimi.ingsw.model.weapons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HarmfulEffect extends Effect {
    private HarmType type;

    /**
     * @param value number to be assigned to value attribute
     * @param type Mark or Damage?
     */
    @JsonCreator
    public HarmfulEffect(@JsonProperty("amount") int value,
                         @JsonProperty("type") HarmType type){
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
