package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.PowerUp;
import javafx.scene.effect.DropShadow;

public class PowerUpGUI extends ResizableImage {

    public PowerUpGUI(PowerUp powerup) {
        super("/gui/powerup/" +
                powerup.getType().toString().toLowerCase() + "_" +
                powerup.getAmmo().toString().toLowerCase() + ".png");
        setEffect(new DropShadow());
    }
}
