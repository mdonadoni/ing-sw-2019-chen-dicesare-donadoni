package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.common.StandingsItem;
import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.model.minified.MiniModel;
import it.polimi.ingsw.view.gui.component.UserViewGUI;
import it.polimi.ingsw.view.gui.util.Notification;
import it.polimi.ingsw.view.gui.util.Selectable;
import it.polimi.ingsw.view.gui.util.StandingsPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The GUI view
 */
public class ViewGUI extends Application {
    /**
     * Main stage
     */
    Stage primaryStage;
    /**
     *
     */
    DummyViewGUI dummy;
    /**
     * Main Pane
     */
    UserViewGUI mainPane;

    /**
     * Start the GUI view
     * @param primaryStage the primary stage of the GUI
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Create mainPane for game view
        mainPane = new UserViewGUI();

        // Construct dummy view
        dummy = new DummyViewGUI(this);

        primaryStage.setTitle("Adrenalina AM26");

        // Initialize login panel
        LoginPane login = new LoginPane();
        login.setLoginCallback(dummy::loginCallback);
        login.connectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                primaryStage.getScene().setRoot(new WaitingPane());
            }
        });

        primaryStage.setScene(new Scene(login, 854, 480));
        primaryStage.setMinWidth(854);
        primaryStage.setMinHeight(480);
        primaryStage.show();
    }

    /**
     * Stop the GUI view, close the connection
     */
    @Override
    public void stop() {
        dummy.closeConnection();
        // fix to close application
        System.exit(0);
    }

    /**
     * Select on the GUI
     * @param objUuid List of objects
     * @param min the minimum object
     * @param max the maximum object
     * @param dialog the dialog to set on the main pane
     * @return Selected object
     */
    public List<String> selectObject(List<String> objUuid, int min, int max, Dialog dialog) {
        // find selectables
        List<Selectable> selectables = new ArrayList<>();
        for (String uuid : objUuid) {
            Selectable s = mainPane.findSelectable(uuid);
            if (s == null) {
                throw new RuntimeException("Selectable not found: " + uuid);
            }
            selectables.add(s);
        }

        // Start SelectionManager
        SelectionManager selectionManager = new SelectionManager(selectables, mainPane.getConfirmButton(), min, max);
        selectionManager.start();
        // Put dialog text
        mainPane.setDialog(dialog, Integer.toString(min), Integer.toString(max));
        // Get selected
        List<String> selected = selectionManager.getSelected();
        // Remove dialog text
        mainPane.removeDialog();
        return selected;
    }

    /**
     * Show a message
     * @param message the message to show
     */
    public void showMessage(String message) {
        Platform.runLater(() -> Notification.newNotification(message));
    }

    public void updateModel(MiniModel model) {
        mainPane.setModel(model);
        Platform.runLater(() -> primaryStage.getScene().setRoot(mainPane));
    }

    public void notifyEndMatch(List<StandingsItem> standings) {
        Platform.runLater(() -> primaryStage.getScene().setRoot(new StandingsPane(standings)));
    }

    /**
     * Handle the disconnection of the client
     */
    public void disconnect() {
        Platform.runLater(() -> {
            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
            dialog.initOwner(primaryStage);
            dialog.setTitle("Connessione con il server chiusa");
            dialog.setHeaderText("La connessione con il server è stata chiusa.");
            dialog.setContentText("Vuoi chiudere l'applicazione?");
            Optional<ButtonType> res = dialog.showAndWait();
            if (res.get() == ButtonType.OK) {
                Platform.exit();
            }
        });
    }

    /**
     * Main of the class
     * @param args parameter from the command line
     */
    public static void main(String[] args) {
        launch(args);
    }

}