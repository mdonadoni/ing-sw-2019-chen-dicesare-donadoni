package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Turn for the client.
 */
public class MiniTurn implements Serializable {
    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = -1196895368824964464L;
    /**
     * Nickname of the current player playing.
     */
    private final String currentPlayer;
    /**
     * List of actions avaible to the current player playing.
     */
    private final ArrayList<MiniAction> avaibleActions;

    @JsonCreator
    private MiniTurn(){
        currentPlayer = null;
        avaibleActions = null;
    }

    /**
     * Constructor of MiniTurn from the current full Player and wheter we are
     * in final frenzy or not.
     * @param currentPlayer Current player playing.
     * @param finalfrenzy Whether we are in final frenzy or not.
     */
    public MiniTurn(Player currentPlayer, boolean finalfrenzy){
        this.currentPlayer = currentPlayer.getNickname();
        this.avaibleActions = new ArrayList<>();
        for(Action action : currentPlayer.supplyActions(finalfrenzy))
            avaibleActions.add(new MiniAction(action));
    }

    /**
     * Get the available actions.
     * @return List of available actions.
     */
    public List<MiniAction> getAvaibleActions(){
        return new ArrayList<>(avaibleActions);
    }

    /**
     * Get nickname of current player.
     * @return Nickname of current player.
     */
    public String getCurrentPlayer(){
        return currentPlayer;
    }
}
