package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.weapons.Attack;

import java.io.Serializable;

public class MiniAttack extends MiniIdentifiable implements Serializable {
    private static final long serialVersionUID = -5252351186695996350L;

    private final String id;
    private final MiniMovement bonusMov;

    @JsonCreator
    public MiniAttack(){
        id = null;
        bonusMov = null;
    }

    public MiniAttack(Attack atk){
        super(atk.getUuid());
        this.id = atk.getDescriptionId();

        if(atk.hasBonusMovement()){
            bonusMov = new MiniMovement(atk.getBonusMovement().get(0));
        }  else {
            bonusMov = null;
        }
    }

    public String getId(){
        return id;
    }

    public boolean hasBonusMovement() {
        return bonusMov != null;
    }

    public MiniMovement getBonusMovement() {
        return bonusMov;
    }
}
