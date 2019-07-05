package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.minified.MiniSquare;
import it.polimi.ingsw.model.minified.MiniStandardSquare;
import it.polimi.ingsw.view.gui.util.Selectable;
import it.polimi.ingsw.view.gui.util.SelectableComponent;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Represent the square in the GUI
 */
public class SquareGUI extends GridPane implements Selectable {
    /**
     * Number of columns
     */
    private static final int NUM_COL = 3;
    /**
     * Number of rows
     */
    private static final int NUM_ROW = 3;
    /**
     * Number of columns for ammo tile
     */
    private static final int NUM_COL_AMMO = 2;
    /**
     * Number of rows for ammo tile
     */
    private static final int NUM_ROW_AMMO = 2;
    /**
     * Row for the players tokens
     */
    private static final Integer[] ROW_TOKEN = new Integer[]{0, 1, 2, 2, 2};
    /**
     * Columns for the players tokens
     */
    private static final Integer[] COL_TOKEN = new Integer[]{2, 2, 2, 1, 0};

    /**
     * Square to represent
     */
    MiniSquare square;
    /**
     * Selectable component
     */
    SelectableComponent select;

    /**
     * Constructor, set the square as selectable add the players token if there are player
     * on the square
     * @param square The square to represent
     */
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

    /**
     * Constructor for std square, like for the square but add the ammo tile
     * @param stdSquare standard square to represent
     */
    public SquareGUI(MiniStandardSquare stdSquare) {
        this((MiniSquare) stdSquare);
        if (stdSquare.hasAmmo()) {
            AmmoTileGUI ammoTileGUI = new AmmoTileGUI(stdSquare.getAmmoTile());
            add(ammoTileGUI, 0, 0, NUM_COL_AMMO, NUM_ROW_AMMO);
        }
    }

    /**
     * Get UUID
     * @return the UUID
     */
    @Override
    public String getUuid() {
        return select.getUuid();
    }

    /**
     * Enable the square
     * @param notifyChange the change notifier
     */
    @Override
    public void enable(Runnable notifyChange) {
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM)));
        select.enable(notifyChange);
    }

    /**
     * Set as selected
     * @param selected true as selected or false as not selected
     */
    @Override
    public void setSelected(boolean selected) {
        select.setSelected(selected);
    }

    /**
     * Disable the square
     */
    @Override
    public void disable() {
        setBorder(null);
        select.disable();
    }
}
