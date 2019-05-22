package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.minified.MiniPlayer;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class PlayerBoardGUI extends GridPane {

    public PlayerBoardGUI(MiniPlayer player){

        ColumnConstraints colMain = new ColumnConstraints();
        colMain.setPercentWidth(100);

        getColumnConstraints().add(colMain);

        RowConstraints row = new RowConstraints();
        row.setPercentHeight(50);

        for(int i=0; i<2; i++)
            getRowConstraints().add(row);

        add(new PlayerPaneGUI(player), 0, 0);
    }
}
