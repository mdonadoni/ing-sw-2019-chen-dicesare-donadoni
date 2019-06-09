package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MiniTurn implements Serializable {
    private static final long serialVersionUID = -1196895368824964464L;

    private final String currentPlayer;
    private final List<MiniAction> avaibleActions;

    @JsonCreator
    private MiniTurn(){
        currentPlayer = null;
        avaibleActions = null;
    }

    public MiniTurn(Player currentPlayer, boolean finalfrenzy){
        this.currentPlayer = currentPlayer.getNickname();
        this.avaibleActions = new ArrayList<>();
        for(Action action : currentPlayer.supplyActions(finalfrenzy))
            avaibleActions.add(new MiniAction(action));
    }

    public List<MiniAction> getAvaibleActions(){
        return new ArrayList<>(avaibleActions);
    }

    public String getCurrentPlayer(){
        return currentPlayer;
    }
}
