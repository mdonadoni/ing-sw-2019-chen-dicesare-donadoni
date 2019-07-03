package it.polimi.ingsw.controller;

import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;

import java.util.Map;

public class Notifier {
    private Map<String, RemotePlayer> remotePlayers;
    private Match match;


    public Notifier(Map<String, RemotePlayer> remotePlayers, Match match){
        this.remotePlayers = remotePlayers;
        this.match = match;
    }

    public void notifyEveryone(Dialog dialogType, String...params){
        for(Player player : match.getActivePlayers()){
            remotePlayers.get(player.getNickname()).safeShowMessage(dialogType, params);
        }
    }
}
