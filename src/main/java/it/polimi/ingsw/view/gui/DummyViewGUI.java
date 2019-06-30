package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.model.minified.MiniModel;
import it.polimi.ingsw.network.LocalView;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class DummyViewGUI extends LocalView {
    private static final Logger LOG = Logger.getLogger(LocalView.class.getName());
    ViewGUI gui;

    public DummyViewGUI(ViewGUI gui) {
        this.gui = gui;
    }

    @Override
    public synchronized ArrayList<String> selectObject(ArrayList<String> objUuid, int min, int max, Dialog dialog) {
        return new ArrayList<>(gui.selectObject(objUuid, min, max, dialog));
    }

    @Override
    public void showMessage(String message) {
        gui.showMessage(message);
    }

    @Override
    public synchronized void updateModel(MiniModel model) {
        gui.updateModel(model);
    }

    public synchronized void loginCallback(LoginInfo info, Consumer<LoginResult> callback) {
        try {
            connectServer(info.getAddress(), info.getPort(), info.getType());
            if (getServer().login(info.getNickname(), this)) {
                callback.accept(LoginResult.LOGIN_SUCCESSFUL);
                return;
            }
        } catch (Exception e) {
            callback.accept(LoginResult.CONNECTION_ERROR);
            return;
        }
        callback.accept(LoginResult.NICKNAME_NOT_VALID);
    }

    @Override
    public synchronized void disconnect() {
        // Call disconnect on LocalView to close the connections
        super.disconnect();
        // Call disconnect on the gui
        gui.disconnect();
    }
}
