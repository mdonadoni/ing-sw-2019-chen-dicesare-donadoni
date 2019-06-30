package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.Identifiable;
import it.polimi.ingsw.model.weapons.Attack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MiniAttack extends Identifiable implements Serializable {
    private static final long serialVersionUID = -5252351186695996350L;

    private final String id;
    private final List<MiniAttack> additionals;

    @JsonCreator
    public MiniAttack(){
        id = null;
        additionals = null;
    }

    public MiniAttack(Attack atk){
        super(atk.getUuid());
        this.id = atk.getDescriptionId();

        additionals = new ArrayList<>();
        if(atk.hasAdditionalAttacks()){
            for(Attack add : atk.getAdditionalAttacks())
                additionals.add(new MiniAttack(add));
        }
    }

    public String getId(){
        return id;
    }

    public List<MiniAttack> getAdditionalAttacks(){
        return new ArrayList<>(additionals);
    }
}
