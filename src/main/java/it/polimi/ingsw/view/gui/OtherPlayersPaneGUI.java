package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.minified.MiniPlayer;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

public class OtherPlayersPaneGUI extends GridPane {
    private static int MAX_OTHER_PLAYERS = 4;

    public OtherPlayersPaneGUI(List<MiniPlayer> players){
        int numberOfPlayers = players.size();

        GridUtils.setPercentColumns(this, 100);

        vgapProperty().bind(heightProperty().multiply(0.02));

        RowConstraints row = new RowConstraints();
        row.setPercentHeight(100.0/MAX_OTHER_PLAYERS);
        for(int i = 0; i < MAX_OTHER_PLAYERS; i++)
            getRowConstraints().add(row);

        for(int i=0; i<numberOfPlayers; i++)
            add(new PlayerPaneGUI(players.get(i)), 0, i);

        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
    }
}
