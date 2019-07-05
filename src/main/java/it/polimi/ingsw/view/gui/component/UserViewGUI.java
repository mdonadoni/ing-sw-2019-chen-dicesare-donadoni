package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.common.dialogs.Dialogs;
import it.polimi.ingsw.model.minified.MiniModel;
import it.polimi.ingsw.view.gui.util.ImageManager;
import it.polimi.ingsw.view.gui.util.Selectable;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Represent the user view
 */
public class UserViewGUI extends BorderPane {
    /**
     * The path of the background
     */
    private static final String BACKGROUND_PATH = "/gui/background.jpg";
    /**
     * The size of the font
     */
    private static final double FONT_SIZE = 20;
    /**
     * The confirm dialog
     */
    private static final String CONFIRM = Dialogs.getDialog(Dialog.CONFIRM);
    /**
     * Confirm button
     */
    private Button confirmButton;
    /**
     * Model GUI
     */
    private ModelGUI modelGUI = null;
    /**
     * Dialog
     */
    private Text textDialog;
    /**
     * Points of the user
     */
    private Text textPoints;
    /**
     * The current player
     */
    private Text textCurrentPlayer;

    /**
     * Constructor, set the background, the button, shows the points, the current player
     */
    public UserViewGUI() {
        // Set background
        ImagePattern pattern = new ImagePattern(ImageManager.getResourceImage(BACKGROUND_PATH));
        BackgroundFill fill = new BackgroundFill(pattern, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(fill);
        setBackground(background);

        // Confirm button
        confirmButton = new Button(CONFIRM);
        confirmButton.setDisable(true);

        Font font  = new Font(FONT_SIZE);
        // Points
        textPoints = new Text();
        textPoints.setFill(Color.WHITE);
        textPoints.setFont(font);
        setPoints(0);

        // Current Player
        textCurrentPlayer = new Text();
        textCurrentPlayer.setFill(Color.WHITE);
        textCurrentPlayer.setFont(font);

        // Bottom
        HBox hbox = new HBox();
        hbox.setSpacing(20);
        hbox.getChildren().addAll(confirmButton, textPoints, textCurrentPlayer);
        setBottom(hbox);

        // Dialog text
        textDialog = new Text();
        textDialog.setFill(Color.WHITE);
        textDialog.setFont(font);
        TextFlow flow = new TextFlow(textDialog);
        setTop(flow);
    }

    /**
     * Set the model
     * @param model the model to set
     */
    public void setModel(MiniModel model) {
        modelGUI = new ModelGUI(model);
        Platform.runLater(() -> {
            setCenter(modelGUI);
            setPoints(model.getMyPoints());
            setCurrentPlayer(model.getMatch().getCurrentTurn().getCurrentPlayer());
        });
    }

    /**
     * Find selectable
     * @param uuid The UUID of the selectable
     * @return the model selectable
     */
    public Selectable findSelectable(String uuid) {
        return modelGUI.findSelectable(uuid);
    }

    /**
     * Get the confirm button
     * @return the confirm button
     */
    public Button getConfirmButton() {
        return confirmButton;
    }

    /**
     * Set the dialog
     * @param dialog dialog to set
     * @param params parameters to fill the blank in the dialog
     */
    public void setDialog(Dialog dialog, String ...params) {
        String message = Dialogs.getDialog(dialog, params);
        Platform.runLater(() -> textDialog.setText(message));
    }

    /**
     * Set points
     * @param points The amount of points
     */
    private void setPoints(int points) {
        textPoints.setText(Dialogs.getDialog(Dialog.POINTS, Integer.toString(points)));
    }

    /**
     * Set current player
     * @param player the current player
     */
    private void setCurrentPlayer(String player) {
        textCurrentPlayer.setText(Dialogs.getDialog(Dialog.CURRENT_PLAYER, player));
    }

    /**
     * Remove the dialog
     */
    public void removeDialog() {
        Platform.runLater(() -> textDialog.setText(""));
    }
}
