package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.minified.MiniWeapon;
import javafx.scene.layout.GridPane;

import java.util.List;

public class WeaponPaneGUI extends GridPane {
    private static final double MAXWEAPONS = 3;

    public WeaponPaneGUI(List<MiniWeapon> weapons, boolean reduced){
        GridUtils.setPercentColumns(this, 100.0/3, 100.0/3, 100.0/3);
        GridUtils.setPercentRows(this, 100.0);

        hgapProperty().bind(widthProperty().multiply(0.05));
        vgapProperty().bind(heightProperty().multiply(0.05));


        for(int i=0; i<weapons.size() && i<MAXWEAPONS; i++)
            add(new WeaponGUI(weapons.get(i), reduced), i, 0);
    }
}
