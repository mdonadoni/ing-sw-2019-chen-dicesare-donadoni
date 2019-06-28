package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TurnController {
    private static final Logger LOG = Logger.getLogger(TurnController.class.getName());
    private static final int NUMBER_OF_MOVES = 2;
    private static final int TURN_MAX_TIME = 180000;

    private Match match;
    private int movesLeft;
    private Player currentPlayer;
    private Map<String, RemotePlayer> remoteUsers;
    private ActionController actionController;
    private RemotePlayer remotePlayer;
    private Updater updater;
    private ScoreController scoreController;

    /**
     * Standard TurnController constructor, initialises everything it needs
     * @param match the current match
     * @param remoteUsers a map containing all the RemotePlayer objects
     */
    public TurnController(Match match, Map<String, RemotePlayer> remoteUsers, Updater updater){
        this.match = match;
        this.remoteUsers = remoteUsers;
        actionController = new ActionController(match, remoteUsers, updater);
        this.updater = updater;
        this.scoreController = new ScoreController(match);
    }

    private void initTurn(){
        movesLeft = NUMBER_OF_MOVES;
        currentPlayer = match.getCurrentTurn().getCurrentPlayer();
        remotePlayer = remoteUsers.get(currentPlayer.getNickname());
        remotePlayer.setTimeLeft(TURN_MAX_TIME);
    }

    public void startTurn() throws RemoteException {
        initTurn();
        // Sending latest model to the current player
        updater.updateModel(currentPlayer.getNickname());
        LOG.log(Level.INFO, "A turn has started for {0}", currentPlayer.getNickname());

        // Do maximum NUMBER_OF_MOVES actions and use powerups
        while(movesLeft > 0){
            selectWhatToDo();
        }

        // EOT: Reload your weapons
        actionController.handleReload(currentPlayer.getNickname());
        LOG.log(Level.INFO, "Turn ended!");

        // Check score updates in case someone died (Press F to pay respect)
        scoreController.lookForScoreUpdates();
    }

    private void selectWhatToDo() throws RemoteException{
        List<Action> availableActions = currentPlayer.supplyActions(match.getFinalFrenzy());

        Action selectedAction = remotePlayer.selectIdentifiable(availableActions, 1, 1, SelectDialog.ACTION_SELECT_DIALOG).get(0);

        LOG.log(Level.INFO, "Action selected: {0}", selectedAction.info());

        actionController.performAction(currentPlayer, selectedAction);

        // Some actions are free (like using a powerup)
        if(selectedAction.expendsUse())
            movesLeft--;
        // Others dictate the end of your turn (reload), just like moving troops in Risiko
        if(selectedAction.endsTurn())
            movesLeft = 0;
    }
}
