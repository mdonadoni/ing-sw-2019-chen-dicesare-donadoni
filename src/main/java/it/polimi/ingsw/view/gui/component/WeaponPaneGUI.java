package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.minified.MiniWeapon;
import it.polimi.ingsw.view.gui.util.GridUtils;
import it.polimi.ingsw.view.gui.util.Selectable;
import it.polimi.ingsw.view.gui.util.SelectableContainer;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent the weapon pane
 */
public class WeaponPaneGUI extends GridPane implements SelectableContainer {
    /**
     * Maximum amount of weapons
     */
    private static final double MAXWEAPONS = 3;
    /**
     * List of weapons GUI
     */
    List<WeaponGUI> weaponsGUI = new ArrayList<>();

    /**
     * Constructor of the class
     * @param weapons List of weapon
     * @param reduced if it's reduced
     */
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

    /**
     * Find selectable (weapon or attack)
     * @param uuid UUID of the selectable
     * @return the selectable (weapon or attack)
     */
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
