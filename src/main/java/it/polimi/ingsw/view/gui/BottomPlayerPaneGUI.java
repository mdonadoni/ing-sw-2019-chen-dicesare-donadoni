package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.PowerUp;
import it.polimi.ingsw.model.minified.MiniWeapon;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.List;

public class BottomPlayerPaneGUI extends GridPane {
    private static final double WEAPONSPACE = 50;
    public BottomPlayerPaneGUI(List<MiniWeapon> weapons, List<PowerUp> powerUps){
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(100);
        getRowConstraints().add(row);
        ColumnConstraints weaponCol = new ColumnConstraints();
        ColumnConstraints pwuCol = new ColumnConstraints();
        weaponCol.setPercentWidth(WEAPONSPACE);
        pwuCol.setPercentWidth(100-WEAPONSPACE);

        getColumnConstraints().add(weaponCol);
        getColumnConstraints().add(pwuCol);

        add(new WeaponPaneGUI(weapons), 0, 0);
    }
}
