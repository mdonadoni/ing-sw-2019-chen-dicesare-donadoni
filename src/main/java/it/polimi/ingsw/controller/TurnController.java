package it.polimi.ingsw.controller;

import it.polimi.ingsw.common.ServerConfig;
import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller that handles a single turn
 */
public class TurnController {
    private static final Logger LOG = Logger.getLogger(TurnController.class.getName());
    /**
     * The maximum number of moves that can be performed in a single turn
     */
    private static final int NUMBER_OF_MOVES = 2;
    /**
     * Maximum time available to perform the turn
     */
    private static final long TURN_MAX_TIME = ServerConfig.getTurnTimeout() * 1000L;

    /**
     * The match going on
     */
    private Match match;
    /**
     * How many moves has the player left
     */
    private int movesLeft;
    /**
     * The current turn's player
     */
    private Player currentPlayer;
    /**
     * Map that matches RemotePlayers with their nickname
     */
    private Map<String, RemotePlayer> remoteUsers;
    /**
     * Controller of the actions
     */
    private ActionController actionController;
    /**
     * The current remote player
     */
    private RemotePlayer remotePlayer;
    /**
     * To update people
     */
    private Updater updater;
    /**
     * The controller of the scores
     */
    private ScoreController scoreController;

    /**
     * Standard TurnController constructor, initialises everything it needs
     * @param match the current match
     * @param remoteUsers a map containing all the RemotePlayer objects
     * @param updater contains method to send updates to the players
     */
    public TurnController(Match match, Map<String, RemotePlayer> remoteUsers, Updater updater){
        this.match = match;
        this.remoteUsers = remoteUsers;
        actionController = new ActionController(match, remoteUsers, updater);
        this.updater = updater;
        this.scoreController = new ScoreController(match);
    }

    /**
     * Initialises the controller for a new turn
     */
    private void initTurn(){
        movesLeft = NUMBER_OF_MOVES;
        currentPlayer = match.getCurrentTurn().getCurrentPlayer();
        remotePlayer = remoteUsers.get(currentPlayer.getNickname());
        remotePlayer.setTimeLeft(TURN_MAX_TIME);
    }

    /**
     * Handles all the operations needed to perform a turn
     * @throws RemoteException In case something goes wrong with the connection
     */
    public void startTurn() throws RemoteException {
        initTurn();
        // Sending latest model to the current player
        updater.updateModelToEveryone();
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

    /**
     * Makes the user choose the action he wants to do
     * @throws RemoteException In case something goes wrong with the connection
     */
    private void selectWhatToDo() throws RemoteException{
        List<Action> availableActions = currentPlayer.supplyActions(match.getFinalFrenzy());

        Action selectedAction = remotePlayer.selectIdentifiable(availableActions, 1, 1, Dialog.ACTION_SELECT).get(0);

        LOG.log(Level.INFO, "Action selected: {0}", selectedAction.info());

        actionController.performAction(currentPlayer, selectedAction);

        // Some actions are free (like using a powerup)
        if(selectedAction.expendsUse())
            movesLeft--;
        // Others dictate the end of your turn (reload), just like moving troops in Risiko
        if(selectedAction.getEndsTurn())
            movesLeft = 0;
    }
}
