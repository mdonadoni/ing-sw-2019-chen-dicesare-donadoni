package it.polimi.ingsw.controller;

import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;

import java.util.Map;

/**
 * This class is for notifying people that something has happened
 */
public class Notifier {
    /**
     * Map matching remote players with their nicknames
     */
    private Map<String, RemotePlayer> remotePlayers;
    /**
     * The match going on
     */
    private Match match;


    /**
     * Constructor
     * @param remotePlayers The map with all the RemotePlayers
     * @param match The match going on
     */
    public Notifier(Map<String, RemotePlayer> remotePlayers, Match match){
        this.remotePlayers = remotePlayers;
        this.match = match;
    }

    /**
     * This method sends everyone a message of the specified type with the specified parameters
     * @param dialogType The type of the dialog
     * @param params Optional parameters to be inserted into the message string
     */
    public void notifyEveryone(Dialog dialogType, String...params){
        for(Player player : match.getActivePlayers()){
            remotePlayers.get(player.getNickname()).safeShowMessage(dialogType, params);
        }
    }
}
