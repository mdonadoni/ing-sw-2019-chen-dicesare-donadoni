package it.polimi.ingsw.view.gui.util;

import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.List;

/**
 * It's used to define some settings for the grid
 */
public class GridUtils {
    /**
     * This class should not be constructed.
     */
    private GridUtils() {}

    /**
     * Create a new GridPane
     * @param columns list of columns
     * @param rows list of rows
     * @return the created GridPane
     */
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

    /**
     * Create a new PaddingPane
     * @param node The node to add in the pane
     * @param pad the amount of padding
     * @return the created PaddingPane
     */
    public static GridPane newPaddingPane(Node node, double pad) {
        GridPane pane = new GridPane();
        setPercentColumns(pane, pad, 100.0 - 2*pad, pad);
        setPercentRows(pane, pad, 100.0 - 2*pad, pad);
        pane.add(node, 1, 1);
        return pane;
    }

    /**
     * Set percent of columns
     * @param grid the grid to modify
     * @param cols the columns to set
     */
    public static void setPercentColumns(GridPane grid, double ...cols) {
        grid.getColumnConstraints().clear();
        for (double col : cols) {
            ColumnConstraints constraint = new ColumnConstraints();
            constraint.setPercentWidth(col);
            grid.getColumnConstraints().add(constraint);
        }
    }
    /**
     * Set percent of rows
     * @param grid the grid to modify
     * @param rows the rows to set
     */
    public static void setPercentRows(GridPane grid, double ...rows) {
        grid.getRowConstraints().clear();
        for (double row : rows) {
            RowConstraints constraint = new RowConstraints();
            constraint.setPercentHeight(row);
            grid.getRowConstraints().add(constraint);
        }
    }
}
