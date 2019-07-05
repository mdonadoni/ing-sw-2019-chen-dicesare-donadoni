package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.weapons.MovementEffect;

import java.io.Serializable;

/**
 * Movement for the client.
 */
public class MiniMovement extends MiniIdentifiable implements Serializable {
    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = 8535981126661456146L;

    @JsonCreator
    private MiniMovement(){
    }

    /**
     * Construct a MiniMovement based on the full Movement.
     * @param effect Movement to be copied.
     */
    public MiniMovement(MovementEffect effect){
        super(effect.getUuid());
    }
}
