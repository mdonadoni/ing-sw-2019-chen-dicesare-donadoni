package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.minified.MiniAmmoTile;
import it.polimi.ingsw.view.gui.util.ResizableImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent the ammo tile in the GUI
 */
public class AmmoTileGUI extends ResizableImage {
    /**
     * Constructor of the class, load the assets for the tile
     * @param tile The tile to represent
     */
    public AmmoTileGUI(MiniAmmoTile tile) {
        // Every asset has the initials of the colors as the name
        // If there is a powerup the name starts with "p"
        List<String> initials = new ArrayList<>();
        tile.getAmmo().forEach(c -> initials.add(c.toString().toLowerCase().substring(0,1)));

        initials.sort(String::compareTo);
        if (tile.hasPowerUp()) {
            initials.add(0, "p");
        }
        String path = "/gui/ammo/" + String.join("", initials) + ".png";
        setImage(path);
    }
}
