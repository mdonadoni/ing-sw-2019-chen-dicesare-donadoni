package it.polimi.ingsw.model.weapons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * This class represent an harmful effect it can applies marks or damages.
 */
public class HarmfulEffect extends Effect {
    /**
     * The type of harmful effect(mark or damage).
     */
    private HarmType type;

    /**
     * Constructor of the class.
     * @param value number to be assigned to value attribute.
     * @param type Mark or Damage?
     */
    @JsonCreator
    public HarmfulEffect(@JsonProperty("amount") int value,
                         @JsonProperty("type") HarmType type){
        setValue(value);
        this.type = type;
    }

    public HarmfulEffect(){

    }

    /**
     * Return the type of the harmful effect: Damage or Mark.
     * @return The type of the harm inflicted: DAMAGE or MARK.
     */
    public HarmType getType() {
        return type;
    }

    /**
     * Set the typer of the harmful effect: Damage or Mark.
     * @param type The type of the harm inflicted. Must be DAMAGE or MARK.
     */
    public void setType(HarmType type) {
        this.type = type;
    }
}
