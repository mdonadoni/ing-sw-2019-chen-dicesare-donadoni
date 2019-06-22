package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class TurnController {
    private static final Logger LOG = Logger.getLogger(TurnController.class.getName());
    private static final int NUMBER_OF_MOVES = 2;
    private static final int TURN_MAX_TIME = 20000;

    private Match match;
    private int movesLeft;
    private Player currentPlayer;
    Map<String, RemotePlayer> remoteUsers;
    private ActionController actionController;
    private RemotePlayer remotePlayer;

    /**
     * Standard TurnController constructor, initialises everything it needs
     * @param match the current match
     * @param remoteUsers a map containing all the RemotePlayer objects
     */
    public TurnController(Match match, Map<String, RemotePlayer> remoteUsers){
        this.match = match;
        movesLeft = NUMBER_OF_MOVES;
        this.remoteUsers = remoteUsers;
        currentPlayer = match.getCurrentTurn().getCurrentPlayer();
        actionController = new ActionController(match, remoteUsers);
        remotePlayer = remoteUsers.get(currentPlayer.getNickname());
    }

    public void startTurn() throws RemoteException {
        remotePlayer.setTimeLeft(TURN_MAX_TIME);

        // Do maximum NUMBER_OF_MOVES actions and use powerups
        while(movesLeft > 0){
            selectWhatToDo();
        }

        // EOT: Reload your weapons
        actionController.handleReload(currentPlayer.getNickname());
    }

    private void selectWhatToDo() throws RemoteException{
        List<Action> availableActions = currentPlayer.supplyActions(match.getFinalFrenzy());

        Action selectedAction = remotePlayer.selectIdentifiable(availableActions, 1, 1).get(0);

        actionController.performAction(currentPlayer, selectedAction);

        // Some actions are free (like using a powerup)
        if(selectedAction.expendsUse())
            movesLeft--;
        // Others dictate the end of your turn (reload), just like moving troops in Risiko
        if(selectedAction.endsTurn())
            movesLeft = 0;
    }
}
