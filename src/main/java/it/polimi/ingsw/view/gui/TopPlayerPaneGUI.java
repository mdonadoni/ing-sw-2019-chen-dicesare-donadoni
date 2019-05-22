package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.PlayerToken;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.List;

public class TopPlayerPaneGUI extends GridPane {

    TopPlayerPaneGUI(String nickname, PlayerToken color, List<PlayerToken> marks){
        RowConstraints markRow = new RowConstraints();
        RowConstraints bottomRow = new RowConstraints();
        RowConstraints topRow = new RowConstraints();
        topRow.setPercentHeight(5);
        markRow.setPercentHeight(65);
        bottomRow.setPercentHeight(30);
        ColumnConstraints padCol = new ColumnConstraints();
        ColumnConstraints markCol = new ColumnConstraints();
        markCol.setPercentWidth(43);
        padCol.setPercentWidth(57);


        getRowConstraints().add(topRow);
        getRowConstraints().add(markRow);
        getRowConstraints().add(bottomRow);
        getColumnConstraints().add(padCol);
        getColumnConstraints().add(markCol);

        add(new MarkTrackPaneGUI(marks), 1, 1);
        add(new NicknamePaneGUI(nickname, color), 0, 1);
    }

}
