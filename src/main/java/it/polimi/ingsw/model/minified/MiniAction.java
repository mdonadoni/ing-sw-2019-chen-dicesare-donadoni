package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.BasicAction;
import it.polimi.ingsw.model.Identifiable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MiniAction extends Identifiable implements Serializable {
    private static final long serialVersionUID = -3216587519484855697L;

    private final ArrayList<BasicAction> basicActions;

    @JsonCreator
    private MiniAction() {
        basicActions = null;
    }

    public MiniAction(Action action){
        super(action.getUuid());
        basicActions = new ArrayList<>(action.getActions());
    }

    public List<BasicAction> getActions(){
        return new ArrayList<>(basicActions);
    }
}
