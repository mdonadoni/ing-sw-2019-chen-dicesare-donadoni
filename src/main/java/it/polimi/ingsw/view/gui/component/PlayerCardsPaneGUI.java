package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.minified.MiniPowerUp;
import it.polimi.ingsw.model.minified.MiniWeapon;
import it.polimi.ingsw.view.gui.util.GridUtils;
import it.polimi.ingsw.view.gui.util.Selectable;
import it.polimi.ingsw.view.gui.util.SelectableContainer;
import javafx.scene.layout.GridPane;

import java.util.List;

/**
 * Represent the player's card pane
 */
public class PlayerCardsPaneGUI extends GridPane implements SelectableContainer {
    /**
     * Space for the weapon
     */
    private static final double WEAPONSPACE = 50;
    /**
     * Set if the weapon is reduced
     */
    boolean reduced;
    /**
     * The weapon pane
     */
    private WeaponPaneGUI weaponPane;
    /**
     * The power-up pane
     */
    private PowerUpPaneGUI powerupPane;

    /**
     * Constructor of the class, show on the gui the player's weapons and power-ups
     * @param weapons List of player's weapon
     * @param powerUps List of player's power-up
     */
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

    /**
     * Constructor with no power-up
     * @param weapons the list of weapons
     */
    public PlayerCardsPaneGUI(List<MiniWeapon> weapons){
        this(weapons, null);
    }

    /**
     * Find selectable object (weapon or power-up)
     * @param uuid UUID of the selectable
     * @return the selectable
     */
    @Override
    public Selectable findSelectable(String uuid) {
        Selectable res = weaponPane.findSelectable(uuid);
        if (res == null && powerupPane != null) res = powerupPane.findSelectable(uuid);
        return res;
    }
}
