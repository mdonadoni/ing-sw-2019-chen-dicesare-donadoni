package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.PowerUp;

public class PowerUpGUI extends FitObject {

    public PowerUpGUI(PowerUp powerup) {
        String path = "/gui/powerup/" +
                powerup.getType().toString().toLowerCase() + "_" +
                powerup.getAmmo().toString().toLowerCase() + ".png";

        StretchImage img = new StretchImage(path);
        setContentWidth(img.getImageWidth());
        setContentHeight(img.getImageHeight());
        getChildren().add(img);
    }
}
