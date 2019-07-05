package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.common.StandingsItem;
import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.model.minified.MiniModel;
import it.polimi.ingsw.network.LocalView;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Adapter for the GUI and the view
 */
public class DummyViewGUI extends LocalView {
    /**
     * The GUI
     */
    ViewGUI gui;

    /**
     * Constructor of the class
     * @param gui the gui to adapt
     */
    public DummyViewGUI(ViewGUI gui) {
        this.gui = gui;
    }

    /**
     * Select between a list of objects.
     * @param objUuid List of the UUID of the objects.
     * @param min Minimum objects.
     * @param max Maximum of objects.
     * @param dialog The dialog type
     * @return selected object
     */
    @Override
    public synchronized ArrayList<String> selectObject(ArrayList<String> objUuid, int min, int max, Dialog dialog) {
        return new ArrayList<>(gui.selectObject(objUuid, min, max, dialog));
    }

    /**
     * Show a message
     * @param message Massage to be shown.
     */
    @Override
    public synchronized void showMessage(String message) {
        gui.showMessage(message);
    }

    /**
     * Update the model
     * @param model Updated model.
     */
    @Override
    public synchronized void updateModel(MiniModel model) {
        gui.updateModel(model);
    }

    /**
     * Notify the end of the match
     * @param standings Final Standings of the game.
     */
    @Override
    public synchronized void notifyEndMatch(ArrayList<StandingsItem> standings) {
        gui.notifyEndMatch(standings);
    }

    /**
     * Login to the server
     * @param info Information for the login
     * @param callback Result of the login
     */
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

    /**
     * Disconnect the view and the GUI
     */
    @Override
    public void disconnect() {
        // Call disconnect on LocalView to close the connections
        super.disconnect();
        // Call disconnect on the gui
        gui.disconnect();
    }
}
