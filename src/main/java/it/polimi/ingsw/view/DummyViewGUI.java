package it.polimi.ingsw.view;

import it.polimi.ingsw.model.minified.MiniModel;
import it.polimi.ingsw.network.LocalView;
import it.polimi.ingsw.view.gui.LoginInfo;
import it.polimi.ingsw.view.gui.LoginResult;

import java.util.List;
import java.util.logging.Logger;
public class DummyViewGUI extends LocalView {
    private static final Logger LOG = Logger.getLogger(LocalView.class.getName());
    ViewGUI gui;

    public DummyViewGUI(ViewGUI gui) {
        this.gui = gui;
    }

    @Override
    public List<String> selectObject(List<String> objUuid, int min, int max) {
        return gui.selectObject(objUuid, min, max);
    }

    @Override
    public void showMessage(String message) {
        gui.showMessage(message);
    }

    @Override
    public void updateModel(MiniModel model) {
        gui.updateModel(model);
    }

    @Override
    public void ping() {
        // Do nothing
    }

    public LoginResult loginCallback(LoginInfo info) {
        try {
            connectServer(info.getAddress(), info.getPort(), info.getType());
            if (getServer().login(info.getNickname(), this)) {
                return LoginResult.LOGIN_SUCCESSFUL;
            }
        } catch (Exception e) {
            return LoginResult.CONNECTION_ERROR;
        }
        return LoginResult.NICKNAME_NOT_VALID;
    }
}
