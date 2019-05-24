package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.minified.MiniWeapon;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.List;

public class WeaponPaneGUI extends GridPane {
    private static final double MAXWEAPONS = 3;

    public WeaponPaneGUI(List<MiniWeapon> weapons){
        RowConstraints row = new RowConstraints();
        ColumnConstraints col = new ColumnConstraints();
        row.setPercentHeight(100);
        col.setPercentWidth(33.3);
        getRowConstraints().add(row);
        for(int i=0; i<MAXWEAPONS; i++)
            getColumnConstraints().add(col);
        for(int i=0; i<weapons.size() && i<MAXWEAPONS; i++)
            add(new WeaponGUI(weapons.get(i).getName()), i, 0);
    }
}
