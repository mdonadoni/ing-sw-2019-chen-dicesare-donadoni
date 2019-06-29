package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.minified.MiniWeapon;
import it.polimi.ingsw.view.gui.util.ImageManager;
import it.polimi.ingsw.view.gui.util.ResizableImage;
import it.polimi.ingsw.view.gui.util.Selectable;
import it.polimi.ingsw.view.gui.util.SelectableComponent;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class WeaponGUI extends ResizableImage implements Selectable {
    MiniWeapon weapon;
    SelectableComponent select;

    public WeaponGUI(MiniWeapon weapon, boolean reduced) {
        this.weapon = weapon;

        String path = "/gui/weapons/" + weapon.getName().toLowerCase() + ".png";
        if (!reduced) {
            setImage(path);
        } else {
            String keyReduced = path + ":reduced";
            if (!ImageManager.hasCustomImage(keyReduced)) {
                Image weaponImage = ImageManager.getResourceImage(path);
                Double width = weaponImage.getWidth();
                Double height = weaponImage.getHeight() * 0.35;

                PixelReader reader = weaponImage.getPixelReader();
                WritableImage reducedImage = new WritableImage(reader, 0, 0, width.intValue(), height.intValue());
                ImageManager.addCustomImage(keyReduced, reducedImage);
            }

            setImage(ImageManager.getCustomImage(keyReduced));
        }

        DropShadow shadow = new DropShadow();

        if (!weapon.isCharged()) {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setSaturation(-1);
            shadow.setInput(colorAdjust);
        }

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
