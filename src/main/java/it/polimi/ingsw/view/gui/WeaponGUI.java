package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.minified.MiniWeapon;
import it.polimi.ingsw.util.ResourceException;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import java.io.InputStream;

public class WeaponGUI extends ResizableImage {

    public WeaponGUI(MiniWeapon weapon, boolean reduced) {
        String path = "/gui/weapons/" + weapon.getName().toLowerCase() + ".png";
        if (!reduced) {
            loadImage(path);
        } else {
            InputStream stream = getClass().getResourceAsStream(path);
            if (stream == null) {
                throw new ResourceException("Couldn't load resource");
            }
            Image weaponImage = new Image(stream);
            Double width = weaponImage.getWidth();
            Double height = weaponImage.getHeight() * 0.35;

            PixelReader reader = weaponImage.getPixelReader();
            WritableImage reducedImage = new WritableImage(reader, 0, 0, width.intValue(), height.intValue());
            setImage(reducedImage);
        }
        setEffect(new DropShadow());
    }
}
