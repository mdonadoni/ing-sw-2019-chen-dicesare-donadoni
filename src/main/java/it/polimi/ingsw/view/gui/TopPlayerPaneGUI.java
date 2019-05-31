package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.PlayerToken;
import javafx.scene.layout.GridPane;

import java.util.List;

public class TopPlayerPaneGUI extends GridPane {

    TopPlayerPaneGUI(String nickname, PlayerToken color, List<PlayerToken> marks){
        GridUtils.setPercentRows(this, 5, 65, 30);
        GridUtils.setPercentColumns(this, 57, 43);

        add(new MarkTrackPaneGUI(marks), 1, 1);
        add(new NicknamePaneGUI(nickname, color), 0, 1);
    }

}
