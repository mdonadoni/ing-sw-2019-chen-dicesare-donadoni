package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.minified.MiniBoard;
import it.polimi.ingsw.view.gui.util.GridUtils;
import it.polimi.ingsw.view.gui.util.Selectable;
import it.polimi.ingsw.view.gui.util.SelectableContainer;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.List;

public class BoardGUI extends GridPane implements SelectableContainer {
    List<SquareGUI> squaresGUI = new ArrayList<>();

    public BoardGUI(MiniBoard board, int numCols, int numRows) {
        double colWidth = 100.0 / numCols;
        double rowHeight = 100.0 / numRows;

        // Add columns
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(colWidth);
        for (int i = 0; i < numCols; i++) {
            getColumnConstraints().add(col);
        }

        // Add rows
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(rowHeight);
        for (int i = 0; i < numRows; i++) {
            getRowConstraints().add(row);
        }

        // Add standard squares
        board.getStandardSquares().forEach((sq) -> {
            SquareGUI squareGUI = new SquareGUI(sq);
            squaresGUI.add(squareGUI);
            add(GridUtils.newPaddingPane(squareGUI, 10), sq.getCoordinates().getColumn(), sq.getCoordinates().getRow());
        });

        // Add spawnpoints
        board.getSpawnPoints().forEach((spawn) -> {
            SquareGUI spawnGUI = new SquareGUI(spawn);
            squaresGUI.add(spawnGUI);
            add(GridUtils.newPaddingPane(spawnGUI, 10), spawn.getCoordinates().getColumn(), spawn.getCoordinates().getRow());
        });
    }

    @Override
    public Selectable findSelectable(String uuid) {
        for (SquareGUI sq : squaresGUI) {
            if (sq.getUuid().equals(uuid)) {
                return sq;
            }
        }
        return null;
    }
}
