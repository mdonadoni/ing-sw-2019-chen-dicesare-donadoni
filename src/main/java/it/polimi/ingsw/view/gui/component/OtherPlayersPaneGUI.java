package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.minified.MiniPlayer;
import it.polimi.ingsw.view.gui.util.GridUtils;
import it.polimi.ingsw.view.gui.util.Selectable;
import it.polimi.ingsw.view.gui.util.SelectableContainer;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class OtherPlayersPaneGUI extends GridPane implements SelectableContainer {
    List<PlayerPaneGUI> playerPanes = new ArrayList<>();

    public OtherPlayersPaneGUI(List<MiniPlayer> players){
        int numberOfPlayers = players.size();

        GridUtils.setPercentColumns(this, 100);

        vgapProperty().bind(heightProperty().multiply(0.02));

        RowConstraints row = new RowConstraints();
        row.setPercentHeight(100.0/players.size());
        for(int i = 0; i < players.size(); i++)
            getRowConstraints().add(row);

        for(int i=0; i<numberOfPlayers; i++) {
            PlayerPaneGUI playerPane = new PlayerPaneGUI(players.get(i));
            playerPanes.add(playerPane);
            add(playerPane, 0, i);
        }

        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    @Override
    public Selectable findSelectable(String uuid) {
        for (PlayerPaneGUI p : playerPanes) {
            Selectable res = p.findSelectable(uuid);
            if (res != null) {
                return res;
            }
        }
        return null;
    }
}
