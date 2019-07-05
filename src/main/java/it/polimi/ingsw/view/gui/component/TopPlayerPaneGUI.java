package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.PlayerToken;
import it.polimi.ingsw.view.gui.util.GridUtils;
import javafx.scene.layout.GridPane;

import java.util.List;

/**
 * Represent the top of the player pane
 */
public class TopPlayerPaneGUI extends GridPane {
    /**
     * Constructor, add the nickname and the mark track
     * @param nickname Nickname of the player
     * @param color Color of the player
     * @param marks List of player's marks
     */
    TopPlayerPaneGUI(String nickname, PlayerToken color, List<PlayerToken> marks){
        GridUtils.setPercentRows(this, 5, 65, 30);
        GridUtils.setPercentColumns(this, 57, 43);

        add(new MarkTrackPaneGUI(marks), 1, 1);
        add(new NicknamePaneGUI(nickname, color), 0, 1);
    }

}
