package it.polimi.ingsw.view.gui;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class SkullTrackPaneGUI extends GridPane {

    SkullTrackPaneGUI(int numberOfSKulls){
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(100);
        ColumnConstraints leftPad = new ColumnConstraints();
        ColumnConstraints rightPad = new ColumnConstraints();
        ColumnConstraints skullCol = new ColumnConstraints();
        leftPad.setPercentWidth(19);
        skullCol.setPercentWidth(34);
        skullCol.setPercentWidth(7.833);

        getRowConstraints().add(row);
        getColumnConstraints().add(leftPad);
        for(int i=0; i<6; i++){
            getColumnConstraints().add(skullCol);
        }
        getColumnConstraints().add(rightPad);

        for(int i=0; i<numberOfSKulls; i++)
            add(new SkullGUI(), i+1, 0);

    }
}
