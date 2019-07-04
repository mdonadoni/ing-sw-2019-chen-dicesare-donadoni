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

public class UserViewGUI extends BorderPane {
    private static final String BACKGROUND_PATH = "/gui/background.jpg";
    private static final double FONT_SIZE = 20;
    private static final String CONFIRM = Dialogs.getDialog(Dialog.CONFIRM);

    private Button confirmButton;
    private ModelGUI modelGUI = null;
    private Text textDialog;
    private Text textPoints;


    public UserViewGUI() {
        // Set background
        ImagePattern pattern = new ImagePattern(ImageManager.getResourceImage(BACKGROUND_PATH));
        BackgroundFill fill = new BackgroundFill(pattern, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(fill);
        setBackground(background);

        // Confirm button
        confirmButton = new Button(CONFIRM);
        confirmButton.setDisable(true);

        // Points
        textPoints = new Text();
        textPoints.setFill(Color.WHITE);
        textPoints.setFont(new Font(FONT_SIZE));
        setPoints(0);

        // Bottom
        HBox hbox = new HBox();
        hbox.setSpacing(20);
        hbox.getChildren().addAll(confirmButton, textPoints);
        setBottom(hbox);

        // Dialog text
        textDialog = new Text();
        textDialog.setFill(Color.WHITE);
        textDialog.setFont(new Font(FONT_SIZE));
        TextFlow flow = new TextFlow(textDialog);
        setTop(flow);
    }

    public void setModel(MiniModel model) {
        modelGUI = new ModelGUI(model);
        Platform.runLater(() -> {
            setCenter(modelGUI);
            setPoints(model.getMyPoints());
        });
    }

    public Selectable findSelectable(String uuid) {
        return modelGUI.findSelectable(uuid);
    }

    public Button getConfirmButton() {
        return confirmButton;
    }

    public void setDialog(Dialog dialog, String ...params) {
        String message = Dialogs.getDialog(dialog, params);
        Platform.runLater(() -> textDialog.setText(message));
    }

    private void setPoints(int points) {
        textPoints.setText(Dialogs.getDialog(Dialog.POINTS, Integer.toString(points)));
    }

    public void removeDialog() {
        Platform.runLater(() -> textDialog.setText(""));
    }
}
