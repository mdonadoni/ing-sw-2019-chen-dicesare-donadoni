package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Identifiable;
import it.polimi.ingsw.model.minified.MiniModel;
import it.polimi.ingsw.network.TimedView;
import it.polimi.ingsw.network.View;

import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RemotePlayer {
    private static final Logger LOG = Logger.getLogger(RemotePlayer.class.getName());

    private String nickname;
    private TimedView timedView;

    RemotePlayer(String nickname, View view) {
        this.nickname = nickname;
        this.timedView = new TimedView(view);
    }

    public String getNickname() {
        return nickname;
    }

    public void setTimeLeft(long timeLeft) {
        timedView.setTimeLeft(timeLeft);
    }

    public void updateModel(MiniModel model, long timeout) throws RemoteException {
        timedView.updateModel(model, timeout);
    }

    public void safeShowMessage(String message) {
        final long showMessageTimeout = 5000;
        try {
            timedView.showMessage(message, showMessageTimeout);
        } catch (RemoteException e) {
            LOG.log(Level.WARNING, () -> "Couldn't send message to " + nickname);
        }
    }

    public void disconnect() throws RemoteException {
        timedView.disconnect();
    }

    public <T extends Identifiable> List<T> selectIdentifiable(List<T> objects, int min, int max) throws RemoteException {
        List<String> uuids = objects
                .stream()
                .map(Identifiable::getUuid)
                .collect(Collectors.toList());
        List<String> selected = timedView.selectObject(uuids, min, max);
        List<T> result = objects
                .stream()
                .filter(obj -> selected.contains(obj.getUuid()))
                .collect(Collectors.toList());
        if (result.size() != selected.size() || result.size() < min || result.size() > max) {
            throw new RemoteException("Something went wrong while selecting");
        }
        return result;
    }

    public boolean isConnected() {
        return timedView.isConnected();
    }
}
