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
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represent the weapon in the GUI
 */
public class WeaponGUI extends ResizableImage implements Selectable, SelectableContainer {
    /**
     * The weapon to represent
     */
    MiniWeapon weapon;
    /**
     * The overlay
     */
    Composition overlay;
    /**
     * Selectable component
     */
    SelectableComponent select;
    /**
     * Map of UUID and selectable
     */
    Map<String, Selectable> attacksSelectable = new HashMap<>();

    /**
     * Constructor, loads the weapon image, overlay the attacks, check if the weapon
     * is charged or not for graphic effects
     * @param weapon the weapon
     * @param reduced if it's reduced
     */
    public WeaponGUI(MiniWeapon weapon, boolean reduced) {
        this.weapon = weapon;

        String path = "/gui/weapons/" + weapon.getName().toLowerCase() + ".png";
        if (!reduced) {

            // Weapon image
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
            BorderStroke stroke = new BorderStroke(
                    Color.RED,
                    BorderStrokeStyle.DASHED,
                    CornerRadii.EMPTY,
                    BorderStroke.MEDIUM);
            Border border = new Border(stroke);
            getImage().setBorder(border);
        }

        getImage().setEffect(shadow);

        select = new SelectableComponent(getImage(), weapon.getUuid());
    }

    /**
     * Find selectable attack
     * @param uuid UUID of the selectable
     * @return the selectable attack
     */
    @Override
    public Selectable findSelectable(String uuid) {
        if (attacksSelectable.containsKey(uuid)) {
            return attacksSelectable.get(uuid);
        }
        return null;
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
     * Enable the overlay
     * @param notifyChange the change notifier
     */
    @Override
    public void enable(Runnable notifyChange) {
        if (overlay != null) {
            overlay.setVisible(false);
        }
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
     * Disable the overlay
     */
    @Override
    public void disable() {
        if (overlay != null) {
            overlay.setVisible(true);
        }
        select.disable();
    }
}
