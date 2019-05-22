package it.polimi.ingsw.view.gui;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;

public class SkullGUI extends GridPane {

    SkullGUI(){

        ColumnConstraints padCol = new ColumnConstraints();
        ColumnConstraints skullCol = new ColumnConstraints();
        RowConstraints padRow = new RowConstraints();
        RowConstraints skullRow = new RowConstraints();
        padCol.setPercentWidth(25);
        skullCol.setPercentWidth(50);
        padRow.setPercentHeight(25);
        skullRow.setPercentHeight(50);

        getColumnConstraints().add(padCol);
        getColumnConstraints().add(skullCol);
        getColumnConstraints().add(padCol);
        getRowConstraints().add(padRow);
        getRowConstraints().add(skullRow);
        getRowConstraints().add(padRow);

        Pane skullPane = new Pane();
        skullPane.setStyle("-fx-background-image: url(/gui/players/marks/skull.png);" +
                        "-fx-background-size: stretch;"+
                        "-fx-effect:  innershadow(two-pass-box, black, 10, 0.2, 2, -2);");
        add(skullPane, 1, 1);
    }

}
