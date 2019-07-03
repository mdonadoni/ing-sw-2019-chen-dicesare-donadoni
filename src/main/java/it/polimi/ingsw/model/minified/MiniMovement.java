package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.weapons.MovementEffect;

import java.io.Serializable;

public class MiniMovement extends MiniIdentifiable implements Serializable {
    private static final long serialVersionUID = 8535981126661456146L;

    @JsonCreator
    public MiniMovement(){
    }

    public MiniMovement(MovementEffect effect){
        super(effect.getUuid());
    }
}
