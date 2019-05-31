package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.minified.MiniBoard;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class BoardGUI extends GridPane {
    public BoardGUI(MiniBoard board, int numCols, int numRows) {
        double colWidth = 100.0 / numCols;
        double rowHeight = 100.0 / numRows;

        hgapProperty().bind(widthProperty().multiply(0.03));
        vgapProperty().bind(heightProperty().multiply(0.03));

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
            add(squareGUI, sq.getCoordinates().getColumn(), sq.getCoordinates().getRow());
        });

        // Add spawnpoints
        board.getSpawnPoints().forEach((spawn) -> {
            SquareGUI spawnGUI = new SquareGUI(spawn);
            add(spawnGUI, spawn.getCoordinates().getColumn(), spawn.getCoordinates().getRow());
        });
    }
}
