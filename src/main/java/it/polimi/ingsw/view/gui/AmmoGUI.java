package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.AmmoColor;
import javafx.scene.layout.*;

public class AmmoGUI extends GridPane {
    public static final double PAD = 25;

    public AmmoGUI(AmmoColor color){
        ColumnConstraints padCol = new ColumnConstraints();
        ColumnConstraints ammoCol = new ColumnConstraints();
        RowConstraints padRow = new RowConstraints();
        RowConstraints ammoRow = new RowConstraints();

        padCol.setPercentWidth(PAD);
        padRow.setPercentHeight(PAD);
        ammoCol.setPercentWidth(100-(PAD*2));
        ammoRow.setPercentHeight(100-(PAD*2));

        getColumnConstraints().add(padCol);
        getColumnConstraints().add(ammoCol);
        getColumnConstraints().add(padCol);
        getRowConstraints().add(padRow);
        getRowConstraints().add(ammoRow);
        getRowConstraints().add(padRow);

        Pane ammopane = new Pane();
        ammopane.setStyle("-fx-background-image: url(/gui/ammo/"+color.toString().toLowerCase()+"Ammo.png);"+
                        "-fx-background-size: stretch;");

        add(ammopane, 1, 1);
    }
}
