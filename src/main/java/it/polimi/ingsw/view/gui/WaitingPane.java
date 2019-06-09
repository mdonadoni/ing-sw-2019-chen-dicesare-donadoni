package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.util.Loader;
import javafx.scene.layout.GridPane;

public class WaitingPane extends GridPane {
    public WaitingPane() {
        Loader.load("/gui/fxml/WaitingPane.fxml", this);
    }
}
