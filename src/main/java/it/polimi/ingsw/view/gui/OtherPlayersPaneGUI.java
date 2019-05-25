package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.PowerUp;
import it.polimi.ingsw.model.minified.MiniPlayer;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.List;

public class OtherPlayersPaneGUI extends GridPane {
    public OtherPlayersPaneGUI(List<MiniPlayer> players){
        int numberOfPlayers = players.size();

        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(100);
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(25);

        getColumnConstraints().add(col);

        for(int i=0; i<4; i++)
            getRowConstraints().add(row);

        for(int i=0; i<numberOfPlayers; i++)
            add(new PlayerBoardGUI(players.get(i), new ArrayList<PowerUp>()), 0, i);
    }
}
