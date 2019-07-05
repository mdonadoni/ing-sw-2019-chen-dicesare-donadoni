package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.AmmoColor;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.List;

/**
 * Represent the Pane for the ammo
 */
public class AmmoPaneGUI extends GridPane {
    /**
     * Number of the columns
     */
    private static final int NCOL = 3;

    public AmmoPaneGUI(List<AmmoColor> ammo){
        ColumnConstraints col = new ColumnConstraints();
        RowConstraints row = new RowConstraints();
        col.setPercentWidth(33.3);
        row.setPercentHeight(33.3);

        for(int i=0; i<NCOL; i++){
            getColumnConstraints().add(col);
            getRowConstraints().add(row);
        }

        for(int i=0; i<ammo.size(); i++){
            add(new AmmoGUI(ammo.get(i)), i%NCOL, i/NCOL);
        }
    }
}
