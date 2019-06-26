package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.minified.MiniSquare;
import it.polimi.ingsw.model.minified.MiniStandardSquare;
import it.polimi.ingsw.view.gui.util.Selectable;
import it.polimi.ingsw.view.gui.util.SelectableComponent;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class SquareGUI extends GridPane implements Selectable {
    private static final int NUM_COL = 3;
    private static final int NUM_ROW = 3;
    private static final int NUM_COL_AMMO = 2;
    private static final int NUM_ROW_AMMO = 2;
    private static final Integer[] ROW_TOKEN = new Integer[]{0, 1, 2, 2, 2};
    private static final Integer[] COL_TOKEN = new Integer[]{2, 2, 2, 1, 0};


    MiniSquare square;
    SelectableComponent select;

    public SquareGUI(MiniSquare square) {
        this.square = square;

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

        setEffect(new DropShadow());

        select = new SelectableComponent(this, square.getUuid());

        for (int i = 0; i < square.getPlayers().size(); i++) {
            add(new TokenGUI(square.getPlayers().get(i), 1), COL_TOKEN[i], ROW_TOKEN[i]);
        }
    }

    public SquareGUI(MiniStandardSquare stdSquare) {
        this((MiniSquare) stdSquare);
        if (stdSquare.hasAmmo()) {
            AmmoTileGUI ammoTileGUI = new AmmoTileGUI(stdSquare.getAmmoTile());
            add(ammoTileGUI, 0, 0, NUM_COL_AMMO, NUM_ROW_AMMO);
        }
    }

    @Override
    public String getUuid() {
        return select.getUuid();
    }

    @Override
    public void enable(Runnable notifyChange) {
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM)));
        select.enable(notifyChange);
    }

    @Override
    public void setSelected(boolean selected) {
        select.setSelected(selected);
    }

    @Override
    public void disable() {
        setBorder(null);
        select.disable();
    }
}
