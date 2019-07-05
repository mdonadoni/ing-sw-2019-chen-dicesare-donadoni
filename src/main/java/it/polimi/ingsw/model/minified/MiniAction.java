package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.BasicAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Action for the client side.
 */
public class MiniAction extends MiniIdentifiable implements Serializable {
    /**
     * Serializable UID
     */
    private static final long serialVersionUID = -3216587519484855697L;
    /**
     * List of basic actions
     */
    private final ArrayList<BasicAction> basicActions;

    @JsonCreator
    private MiniAction() {
        basicActions = null;
    }
    /**
     * Constructor of the class
     * @param action The Action to convert
     */
    public MiniAction(Action action){
        super(action.getUuid());
        basicActions = new ArrayList<>(action.getActions());
    }

    /**
     * Get list of basic action.
     * @return List of basic action.
     */
    public List<BasicAction> getActions(){
        return new ArrayList<>(basicActions);
    }
}
