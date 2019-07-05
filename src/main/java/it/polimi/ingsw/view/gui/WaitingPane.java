package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.util.Loader;
import javafx.scene.layout.GridPane;

/**
 * Represent the waiting lobby
 */
public class WaitingPane extends GridPane {
    /**
     * Constructor of the class, load the pane from file
     */
    public WaitingPane() {
        Loader.load("/gui/fxml/WaitingPane.fxml", this);
    }
}
