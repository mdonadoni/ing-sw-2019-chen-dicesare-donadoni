package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.minified.MiniWeapon;
import it.polimi.ingsw.view.gui.util.GridUtils;
import it.polimi.ingsw.view.gui.util.Selectable;
import it.polimi.ingsw.view.gui.util.SelectableContainer;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class WeaponPaneGUI extends GridPane implements SelectableContainer {
    private static final double MAXWEAPONS = 3;
    List<WeaponGUI> weaponsGUI = new ArrayList<>();

    public WeaponPaneGUI(List<MiniWeapon> weapons, boolean reduced){
        GridUtils.setPercentColumns(this, 100.0/3, 100.0/3, 100.0/3);
        GridUtils.setPercentRows(this, 100.0);

        hgapProperty().bind(widthProperty().multiply(0.05));
        vgapProperty().bind(heightProperty().multiply(0.05));


        for(int i=0; i<weapons.size() && i<MAXWEAPONS; i++) {
            WeaponGUI weapon = new WeaponGUI(weapons.get(i), reduced);
            weaponsGUI.add(weapon);
            add(weapon, i, 0);
        }
    }

    @Override
    public Selectable findSelectable(String uuid) {
        for (WeaponGUI w : weaponsGUI) {
            if (w.getUuid().equals(uuid)) {
                return w;
            }

            Selectable attack = w.findSelectable(uuid);
            if (attack != null) {
                return attack;
            }
        }
        return null;
    }
}
