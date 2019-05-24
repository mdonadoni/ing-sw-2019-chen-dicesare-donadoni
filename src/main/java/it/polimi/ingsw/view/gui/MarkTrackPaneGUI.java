package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.PlayerToken;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.List;

public class MarkTrackPaneGUI extends GridPane {

    MarkTrackPaneGUI(List<PlayerToken> marks){
        RowConstraints markRow = new RowConstraints();
        RowConstraints bottomRow = new RowConstraints();
        bottomRow.setPercentHeight(20);
        markRow.setPercentHeight(80);
        ColumnConstraints markCol = new ColumnConstraints();
        markCol.setPercentWidth(8.33);

        getRowConstraints().add(markRow);
        getRowConstraints().add(bottomRow);

        for(int i=0; i<12; i++)
            getColumnConstraints().add(markCol);

        for(int i=0; i<marks.size(); i++)
            add(new TokenGUI(marks.get(i), 1), i, 0);
    }
}
