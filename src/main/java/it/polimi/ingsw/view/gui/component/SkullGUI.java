package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.view.gui.util.GridUtils;
import it.polimi.ingsw.view.gui.util.StretchImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class SkullGUI extends GridPane {

    SkullGUI(){
        GridUtils.setPercentColumns(this, 25, 50, 25);
        GridUtils.setPercentRows(this, 25, 50, 25);


        Pane skullPane = new Pane();
        /*skullPane.setStyle("-fx-background-image: url(/gui/players/marks/skull.png);" +
                        "-fx-background-size: stretch;"+
                        "-fx-effect:  innershadow(two-pass-box, black, 10, 0.2, 2, -2);");*/
        add(new StretchImage("/gui/players/marks/skull.png"), 1, 1);
    }

}
