package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.minified.MiniGameBoard;
import it.polimi.ingsw.model.minified.MiniPlayer;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.List;

public class UserViewGUI extends GridPane {

    private static final double BOARDSPACE = 55;

    public UserViewGUI(MiniPlayer miniPlayer, List<MiniPlayer> otherPlayers, MiniGameBoard miniGameBoard){

        RowConstraints boardRow = new RowConstraints();
        RowConstraints playerRow = new RowConstraints();
        ColumnConstraints boardCol = new ColumnConstraints();
        ColumnConstraints playerCol = new ColumnConstraints();
        boardRow.setPercentHeight(BOARDSPACE);
        boardCol.setPercentWidth(BOARDSPACE);
        playerCol.setPercentWidth(100-BOARDSPACE);
        playerRow.setPercentHeight(100-BOARDSPACE);

        getColumnConstraints().add(boardCol);
        getColumnConstraints().add(playerCol);
        getRowConstraints().add(boardRow);
        getRowConstraints().add(playerRow);

        add(new GameBoardGUI(miniGameBoard), 0, 0);
        add(new PlayerBoardGUI(miniPlayer), 0, 1);
        add(new OtherPlayersPaneGUI(otherPlayers), 1, 0, 1, 2);
    }
}
