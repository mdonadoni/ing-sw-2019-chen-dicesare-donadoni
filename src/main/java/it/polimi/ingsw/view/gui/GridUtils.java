package it.polimi.ingsw.view.gui;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.List;

public class GridUtils {
    public static GridPane newGridPane(List<Double> columns, List<Double> rows) {
        GridPane pane = new GridPane();
        for (double column : columns) {
            ColumnConstraints constraint = new ColumnConstraints();
            constraint.setPercentWidth(column);
            pane.getColumnConstraints().add(constraint);
        }

        for (double row : rows) {
            RowConstraints constraint = new RowConstraints();
            constraint.setPercentHeight(row);
            pane.getRowConstraints().add(constraint);
        }
        return pane;
    }

    public static void setPercentColumns(GridPane grid, double ...cols) {
        grid.getColumnConstraints().clear();
        for (double col : cols) {
            ColumnConstraints constraint = new ColumnConstraints();
            constraint.setPercentWidth(col);
            grid.getColumnConstraints().add(constraint);
        }
    }

    public static void setPercentRows(GridPane grid, double ...rows) {
        grid.getRowConstraints().clear();
        for (double row : rows) {
            RowConstraints constraint = new RowConstraints();
            constraint.setPercentHeight(row);
            grid.getRowConstraints().add(constraint);
        }
    }
}
