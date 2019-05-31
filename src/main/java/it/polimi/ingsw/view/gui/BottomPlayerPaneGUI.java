package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.PowerUp;
import it.polimi.ingsw.model.minified.MiniWeapon;
import javafx.scene.layout.GridPane;

import java.util.List;

public class BottomPlayerPaneGUI extends GridPane {
    private static final double WEAPONSPACE = 50;

    boolean reduced;

    public BottomPlayerPaneGUI(List<MiniWeapon> weapons, List<PowerUp> powerUps){
        GridUtils.setPercentRows(this, 100);
        if (powerUps == null) {
            reduced = true;
            GridUtils.setPercentColumns(this, 100.0);
        } else {
            reduced = false;
            GridUtils.setPercentColumns(this, WEAPONSPACE, 100.0 - WEAPONSPACE);
        }

        add(new WeaponPaneGUI(weapons, reduced), 0, 0);
        if (!reduced) {
            add(new PowerUpPaneGUI(powerUps), 1, 0);
        }
    }

    public BottomPlayerPaneGUI(List<MiniWeapon> weapons){
        this(weapons, null);
    }
}
