package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;

import java.util.Map;

/**
 * TODO
 */
public class TurnController {
    private Match match;
    private int movesLeft;
    private Player currentPlayer;
    Map<String, RemotePlayer> remoteUsers;

    public TurnController(Match match, Map<String, RemotePlayer> remoteUsers){
        this.match = match;
        movesLeft = 2;
        this.remoteUsers = remoteUsers;
        currentPlayer = match.getCurrentTurn().getCurrentPlayer();
    }

    public void startTurn(){

    }

    private void selectWhatToDo(){
        if(currentPlayer.getPowerUps().size() > 0){
            // Powerups!
        }
        else
            new ActionController(match, remoteUsers);
    }
}
