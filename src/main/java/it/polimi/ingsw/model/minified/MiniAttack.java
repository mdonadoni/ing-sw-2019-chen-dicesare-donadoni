package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.weapons.Attack;

import java.io.Serializable;

/**
 * Attack for the client side.
 */
public class MiniAttack extends MiniIdentifiable implements Serializable {
    /**
     * Serializable UID
     */
    private static final long serialVersionUID = -5252351186695996350L;
    /**
     * The id of the attack.
     */
    private final String id;
    /**
     * The bonus movement effect of the attack
     */
    private final MiniMovement bonusMov;

    @JsonCreator
    private MiniAttack(){
        id = null;
        bonusMov = null;
    }

    /**
     * Constructor of the class
     * @param atk The Attack to convert
     */
    public MiniAttack(Attack atk){
        super(atk.getUuid());
        this.id = atk.getDescriptionId();

        if(atk.hasBonusMovement()){
            bonusMov = new MiniMovement(atk.getBonusMovement().get(0));
        }  else {
            bonusMov = null;
        }
    }

    /**
     * Get identifier of this attack.
     * @return Identifier of this attack.
     */
    public String getId(){
        return id;
    }

    /**
     * Returns whether this attack has a bonus movement.
     * @return True if this attack has a bonus movement, false otherwise.
     */
    public boolean hasBonusMovement() {
        return bonusMov != null;
    }

    /**
     * Returns the bonus movement.
     * @return The bonus movement.
     */
    public MiniMovement getBonusMovement() {
        return bonusMov;
    }
}
