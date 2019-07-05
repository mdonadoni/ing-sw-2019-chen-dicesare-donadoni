package it.polimi.ingsw.controller;

import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.minified.MiniModel;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that handles the model updates
 */
public class Updater {
    private static final Logger LOG = Logger.getLogger(Updater.class.getName());

    /**
     * The maximum number to wait for an update response
     */
    private static final int UPDATE_TIMEOUT = 10000;

    /**
     * Map matching RemotePlayers with their nicknames
     */
    private Map<String, RemotePlayer> remotePlayers;
    /**
     * The match going on
     */
    private Match match;
    /**
     * The controller that notifies player about things
     */
    private Notifier notifier;

    /**
     * Constructor
     * @param remotePlayers The Map with the RemotePlayers
     * @param match The match going on
     */
    public Updater(Map<String, RemotePlayer> remotePlayers, Match match){
        this.remotePlayers = remotePlayers;
        this.match = match;
        notifier = new Notifier(remotePlayers, match);
    }

    /**
     * Sends everyone a model update
     */
    public void updateModelToEveryone(){
        for(Player player : match.getPlayers()){
            if(!remotePlayers.get(player.getNickname()).isConnected() && player.isActive())
                handleUpdateFailure(remotePlayers.get(player.getNickname()));
        }

        for(Player player : match.getActivePlayers()){
            updateModel(player.getNickname());
        }
    }

    /**
     * Sends a specific player a model update
     * @param nickname The player I want to send the update to
     */
    public void updateModel(String nickname){
        RemotePlayer remotePlayer = remotePlayers.get(nickname);
        try {
            LOG.log(Level.INFO, "Sending updates to {0}", nickname);
            remotePlayer.updateModel(new MiniModel(match, match.getPlayerByNickname(nickname)), UPDATE_TIMEOUT);
        } catch (RemoteException e) {
            handleUpdateFailure(remotePlayer);
        }
    }

    /**
     * Handles the failure of an update, also sets the player as inactive and notify everyone that the player
     * has disconnected
     * @param player The player who has disconnected
     */
    private void handleUpdateFailure(RemotePlayer player) {
        LOG.log(Level.WARNING, "Error while sending updates for player {0}", player.getNickname());
        match.getPlayerByNickname(player.getNickname()).setActive(false);
        notifier.notifyEveryone(Dialog.PLAYER_DISCONNECTED, player.getNickname());
    }
}
