package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.LocalServer;
import it.polimi.ingsw.network.View;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class ServerController extends LocalServer {
    private static final Logger LOG = Logger.getLogger(ServerController.class.getName());

    private Set<String> connectedUser = new HashSet<>();

    @Override
    public synchronized void login(String nickname, View view) throws NicknameAlreadyUsedException {
        if (connectedUser.contains(nickname)) {
            LOG.warning(() -> "Username already used: "+ nickname);
            throw new NicknameAlreadyUsedException();
        }
        connectedUser.add(nickname);
        LOG.info(() -> "New user connected: " + nickname);
        try {
            view.showMessage("Il server ti dice ciao!");
        } catch(RemoteException e) {
            LOG.severe(() -> "Cannot greet new user " + nickname);
        }
        try {
            view.disconnect();
        } catch(RemoteException e) {
            LOG.severe(() -> "Cannot disconnect user " + nickname);
        }
    }
}