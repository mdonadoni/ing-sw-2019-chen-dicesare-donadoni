package it.polimi.ingsw.model.weapons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class HarmfulEffect extends Effect {
    private HarmType type;

    /**
     * @param value number to be assigned to value attribute
     * @param type Mark or Damage?
     */
    @JsonCreator
    public HarmfulEffect(@JsonProperty("amount") int value,
                         @JsonProperty("type") HarmType type){
        setValue(value);
        this.type = type;
    }

    HarmfulEffect(JsonNode json){
        setValue(json.get("value").asInt());
        setType(HarmType.valueOf(json.get("harm").asText().toUpperCase()));
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
