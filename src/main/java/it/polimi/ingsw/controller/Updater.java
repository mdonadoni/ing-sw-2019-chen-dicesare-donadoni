package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.minified.MiniModel;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Updater {
    private static final Logger LOG = Logger.getLogger(Updater.class.getName());
    private Map<String, RemotePlayer> remotePlayers;
    Match match;

    public Updater(Map<String, RemotePlayer> remotePlayers, Match match){
        this.remotePlayers = remotePlayers;
        this.match = match;
    }

    public void updateModelToEveryone(){
        for(Player player : match.getPlayers()){
            if(player.isActive())
                updateModel(player.getNickname());
        }
    }

    public void updateModel(String nickname){
        RemotePlayer remotePlayer = remotePlayers.get(nickname);
        try {
            LOG.log(Level.INFO, "Sending updates to {0}", nickname);
            remotePlayer.updateModel(new MiniModel(match, match.getPlayerByNickname(nickname)), 8000);
        } catch (RemoteException e) {
            handleUpdateFailure(remotePlayer);
        }
    }

    private void handleUpdateFailure(RemotePlayer player) {
        LOG.log(Level.WARNING, "Error while sending updates for player {0}", player.getNickname());
        match.getPlayerByNickname(player.getNickname()).setActive(false);
    }
}
