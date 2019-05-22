package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.util.ResourceException;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class AmmoTileGUI extends Pane {
    public AmmoTileGUI(AmmoTile tile) {
        // Every asset has the initials of the colors as the name
        // If there is a powerup the name starts with "p"
        List<String> initials = new ArrayList<>();
        tile.getAmmo().forEach(c -> initials.add(c.toString().toLowerCase().substring(0,1)));

        initials.sort(String::compareTo);
        if (tile.hasPowerUp()) {
            initials.add(0, "p");
        }
        String path = "/gui/ammo/" + String.join("", initials) + ".png";
        if (getClass().getResource(path) == null) {
            throw new ResourceException("Cannot find ammo tile gui");
        }

        setStyle("-fx-background-image: url(" + path + ");" +
                 "-fx-background-size: stretch;");
    }
}
