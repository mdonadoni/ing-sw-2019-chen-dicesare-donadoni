package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.PowerUp;
import it.polimi.ingsw.model.minified.MiniPlayer;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.List;

public class PlayerBoardGUI extends GridPane {

    public PlayerBoardGUI(MiniPlayer player, List<PowerUp> myPowerUps){

        ColumnConstraints colMain = new ColumnConstraints();
        colMain.setPercentWidth(100);

        getColumnConstraints().add(colMain);

        RowConstraints row = new RowConstraints();
        row.setPercentHeight(50);

        for(int i=0; i<2; i++)
            getRowConstraints().add(row);

        add(new PlayerPaneGUI(player), 0, 0);
        add(new BottomPlayerPaneGUI(player.getWeapons(), myPowerUps), 0, 1);
    }
}
