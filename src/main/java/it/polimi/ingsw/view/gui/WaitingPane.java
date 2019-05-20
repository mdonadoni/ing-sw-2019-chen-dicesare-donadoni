package it.polimi.ingsw.view.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class WaitingPane extends GridPane {
    public WaitingPane() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/gui/fxml/WaitingPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
