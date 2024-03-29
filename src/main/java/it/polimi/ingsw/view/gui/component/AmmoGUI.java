package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.view.gui.util.ResizableImage;
import javafx.scene.layout.*;

/**
 * Represent the ammo in the GUI
 */
public class AmmoGUI extends GridPane {
    /**
     * Dimension of the padding
     */
    public static final double PAD = 25;

    /**
     * Constructor of the class
     * @param color ammo to show
     */
    public AmmoGUI(AmmoColor color){
        ColumnConstraints padCol = new ColumnConstraints();
        ColumnConstraints ammoCol = new ColumnConstraints();
        RowConstraints padRow = new RowConstraints();
        RowConstraints ammoRow = new RowConstraints();

        padCol.setPercentWidth(PAD);
        padRow.setPercentHeight(PAD);
        ammoCol.setPercentWidth(100-(PAD*2));
        ammoRow.setPercentHeight(100-(PAD*2));

        getColumnConstraints().add(padCol);
        getColumnConstraints().add(ammoCol);
        getColumnConstraints().add(padCol);
        getRowConstraints().add(padRow);
        getRowConstraints().add(ammoRow);
        getRowConstraints().add(padRow);

        add(new ResizableImage("/gui/ammo/"+color.toString().toLowerCase()+"Ammo.png"), 1, 1);
    }
}
