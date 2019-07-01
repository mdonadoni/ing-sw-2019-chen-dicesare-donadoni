package it.polimi.ingsw.view.gui.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.minified.MiniAttack;
import it.polimi.ingsw.model.minified.MiniMovement;
import it.polimi.ingsw.model.minified.MiniWeapon;
import it.polimi.ingsw.util.Json;
import it.polimi.ingsw.util.ResourceException;
import it.polimi.ingsw.util.ResourceManager;
import it.polimi.ingsw.view.gui.util.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WeaponGUI extends ResizableImage implements Selectable, SelectableContainer {
    MiniWeapon weapon;
    Composition overlay;
    SelectableComponent select;
    Map<String, Selectable> attacksSelectable = new HashMap<>();

    public WeaponGUI(MiniWeapon weapon, boolean reduced) {
        this.weapon = weapon;

        String path = "/gui/weapons/" + weapon.getName().toLowerCase() + ".png";
        if (!reduced) {

            // Powerup image
            setImage(path);

            // Selection for attacks
            overlay = new Composition();
            overlay.setCompositionWidth(getImage().getImageWidth());
            overlay.setCompositionHeight(getImage().getImageHeight());

            try {
                String pathJson = "/gui/weapons/" + weapon.getName().toLowerCase() + ".json";
                ObjectMapper mapper = Json.getMapper();
                JsonNode json = mapper.readTree(ResourceManager.get(pathJson));
                JsonNode attacksJson = json.get("attacks");

                for (MiniAttack atk : weapon.getAttacks()) {
                    String id = atk.getId();
                    SelectableArea attackPane = new SelectableArea(atk.getUuid());
                    attacksSelectable.put(atk.getUuid(), attackPane);
                    overlay.add(attackPane, Position.fromJson(attacksJson.get(id)));

                    if (atk.hasBonusMovement()) {
                        MiniMovement mov = atk.getBonusMovement();
                        SelectableArea bonusPane = new SelectableArea(mov.getUuid());
                        attacksSelectable.put(mov.getUuid(), bonusPane);
                        overlay.add(bonusPane, Position.fromJson(attacksJson.get("movement")));
                    }
                }
            } catch (IOException e) {
                throw new ResourceException("Error while parsing weapon gui json " + weapon.getName().toLowerCase());
            }
            getChildren().add(overlay);
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
    public Selectable findSelectable(String uuid) {
        if (attacksSelectable.containsKey(uuid)) {
            return attacksSelectable.get(uuid);
        }
        return null;
    }

    @Override
    public String getUuid() {
        return select.getUuid();
    }

    @Override
    public void enable(Runnable notifyChange) {
        if (overlay != null) {
            overlay.setVisible(false);
        }
        select.enable(notifyChange);
    }

    @Override
    public void setSelected(boolean selected) {
        select.setSelected(selected);
    }

    @Override
    public void disable() {
        if (overlay != null) {
            overlay.setVisible(true);
        }
        select.disable();
    }
}
