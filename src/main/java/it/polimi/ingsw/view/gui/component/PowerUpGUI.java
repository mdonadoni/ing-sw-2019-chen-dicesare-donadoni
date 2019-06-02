package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.PowerUp;
import it.polimi.ingsw.view.gui.util.ResizableImage;
import it.polimi.ingsw.view.gui.util.Selectable;
import it.polimi.ingsw.view.gui.util.SelectableComponent;
import javafx.scene.effect.DropShadow;

public class PowerUpGUI extends ResizableImage implements Selectable {
    PowerUp powerup;
    SelectableComponent select;

    public PowerUpGUI(PowerUp powerup) {
        super("/gui/powerup/" +
                powerup.getType().toString().toLowerCase() + "_" +
                powerup.getAmmo().toString().toLowerCase() + ".png");
        this.powerup = powerup;

        System.out.println("New powerup: " + powerup.getUuid());

        setEffect(new DropShadow());

        select = new SelectableComponent(this, powerup.getUuid());
    }

    @Override
    public String getUuid() {
        return select.getUuid();
    }

    @Override
    public void enable(Runnable notifyChange) {
        select.enable(notifyChange);
    }

    @Override
    public void setSelected(boolean selected) {
        select.setSelected(selected);
    }

    @Override
    public void disable() {
        select.disable();
    }
}
