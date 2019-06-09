package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.BasicAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MiniAction implements Serializable {
    private static final long serialVersionUID = -3216587519484855697L;

    private final List<BasicAction> basicActions;

    @JsonCreator
    private MiniAction(){
        basicActions = null;
    }

    public MiniAction(Action action){
        basicActions = new ArrayList<>(action.getActions());
    }

    public List<BasicAction> getActions(){
        return new ArrayList<>(basicActions);
    }
}
