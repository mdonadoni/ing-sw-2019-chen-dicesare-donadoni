package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.minified.MiniWeapon;
import it.polimi.ingsw.util.ResourceException;
import it.polimi.ingsw.view.gui.util.Selectable;
import it.polimi.ingsw.view.gui.util.ResizableImage;
import it.polimi.ingsw.view.gui.util.SelectableComponent;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import java.io.InputStream;

public class WeaponGUI extends ResizableImage implements Selectable {
    MiniWeapon weapon;

    DropShadow shadow;
    SelectableComponent select;

    public WeaponGUI(MiniWeapon weapon, boolean reduced) {
        this.weapon = weapon;

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

        shadow = new DropShadow();
        getImage().setEffect(shadow);

        select = new SelectableComponent(getImage(), weapon.getUuid());
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
