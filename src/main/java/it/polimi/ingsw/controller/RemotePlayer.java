package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Identifiable;
import it.polimi.ingsw.network.TimedView;
import it.polimi.ingsw.network.View;

import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;

public class RemotePlayer {
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

    public void showMessage(String message) throws RemoteException {
        timedView.showMessage(message);
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
