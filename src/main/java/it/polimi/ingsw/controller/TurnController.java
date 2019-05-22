package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;

/**
 * TODO
 */
public class TurnController {
    private Match match;
    private int movesLeft;
    private Player currentPlayer;

    public TurnController(Match match){
        this.match = match;
        movesLeft = 2;
        currentPlayer = match.getCurrentTurn().getCurrentPlayer();
    }

    public void startTurn(){

    }

    private void selectWhatToDo(){
        if(currentPlayer.getPowerUps().size() > 0){
            // Powerups!
        }
        else
            new ActionController();
    }
}
