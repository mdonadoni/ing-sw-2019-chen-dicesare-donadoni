package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.minified.MiniPowerUp;
import it.polimi.ingsw.view.gui.util.ResizableImage;
import it.polimi.ingsw.view.gui.util.Selectable;
import it.polimi.ingsw.view.gui.util.SelectableComponent;
import javafx.scene.effect.DropShadow;

/**
 * Represent the power-up in the GUI
 */
public class PowerUpGUI extends ResizableImage implements Selectable {
    /**
     * The power-up
     */
    MiniPowerUp powerup;
    /**
     * The selectable component
     */
    SelectableComponent select;

    /**
     * Constructor, load the power-up image from the file, and turn it as a selectable
     * @param powerup the power-up
     */
    public PowerUpGUI(MiniPowerUp powerup) {
        super("/gui/powerup/" +
                powerup.getType().toString().toLowerCase() + "_" +
                powerup.getAmmo().toString().toLowerCase() + ".png");
        this.powerup = powerup;

        setEffect(new DropShadow());

        select = new SelectableComponent(this, powerup.getUuid());
    }

    /**
     * Get UUID
     * @return the UUID
     */
    @Override
    public String getUuid() {
        return select.getUuid();
    }

    /**
     * Enable the power-up
     * @param notifyChange the change notifier
     */
    @Override
    public void enable(Runnable notifyChange) {
        select.enable(notifyChange);
    }

    /**
     * Set as selected
     * @param selected true as selected or false as not selected
     */
    @Override
    public void setSelected(boolean selected) {
        select.setSelected(selected);
    }

    /**
     * Disable the power-up
     */
    @Override
    public void disable() {
        select.disable();
    }
}
