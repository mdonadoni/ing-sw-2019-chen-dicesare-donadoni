package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.minified.MiniSquare;
import it.polimi.ingsw.model.minified.MiniStandardSquare;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class SquareGUI extends GridPane {
    private static final int NUM_COL = 3;
    private static final int NUM_ROW = 3;
    private static final int NUM_COL_AMMO = 2;
    private static final int NUM_ROW_AMMO = 2;

    MiniSquare square;
    public SquareGUI(MiniSquare square) {
        this.square = square;

        getStyleClass().add("highlighted-square");

        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(100.0/NUM_COL);
        for (int i = 0; i < NUM_COL; i++) {
            getColumnConstraints().add(col);
        }

        RowConstraints row = new RowConstraints();
        row.setPercentHeight(100.0/NUM_ROW);
        for (int i = 0; i < NUM_ROW; i++) {
            getRowConstraints().add(row);
        }

        //TODO put player tokens
    }

    public SquareGUI(MiniStandardSquare stdSquare) {
        this((MiniSquare) stdSquare);
        if (stdSquare.hasAmmo()) {
            AmmoTileGUI ammoTileGUI = new AmmoTileGUI(stdSquare.getAmmoTile());
            add(ammoTileGUI, 0, 0, NUM_COL_AMMO, NUM_ROW_AMMO);
        }
    }
}
