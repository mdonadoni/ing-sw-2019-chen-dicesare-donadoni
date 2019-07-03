package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.minified.MiniPowerUp;
import it.polimi.ingsw.model.minified.MiniWeapon;
import it.polimi.ingsw.view.gui.util.GridUtils;
import it.polimi.ingsw.view.gui.util.Selectable;
import it.polimi.ingsw.view.gui.util.SelectableContainer;
import javafx.scene.layout.GridPane;

import java.util.List;

public class PlayerCardsPaneGUI extends GridPane implements SelectableContainer {
    private static final double WEAPONSPACE = 50;

    boolean reduced;
    private WeaponPaneGUI weaponPane;
    private PowerUpPaneGUI powerupPane;

    public PlayerCardsPaneGUI(List<MiniWeapon> weapons, List<MiniPowerUp> powerUps){
        GridUtils.setPercentRows(this, 100);
        if (powerUps == null) {
            reduced = true;
            GridUtils.setPercentColumns(this, 100.0);
        } else {
            reduced = false;
            GridUtils.setPercentColumns(this, WEAPONSPACE, 100.0 - WEAPONSPACE);
        }

        weaponPane = new WeaponPaneGUI(weapons, reduced);

        add(weaponPane, 0, 0);
        if (!reduced) {
            powerupPane = new PowerUpPaneGUI(powerUps);
            add(powerupPane, 1, 0);
        }
    }

    public PlayerCardsPaneGUI(List<MiniWeapon> weapons){
        this(weapons, null);
    }

    @Override
    public Selectable findSelectable(String uuid) {
        Selectable res = weaponPane.findSelectable(uuid);
        if (res == null && powerupPane != null) res = powerupPane.findSelectable(uuid);
        return res;
    }
}
