package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.AmmoTile;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class AmmoTileGUI extends Pane {
    public AmmoTileGUI(AmmoTile tile) {
        // Every asset has the initials of the colors as the name
        List<String> initials = new ArrayList<>();
        tile.getAmmo().forEach((c) -> initials.add(c.toString().toLowerCase().substring(0,1)));
        if (tile.hasPowerUp()) {
            initials.add("p");
        }
        initials.sort(String::compareTo);
        String path = "/gui/ammo/" + String.join("", initials) + ".png";
        setStyle("-fx-background-image: url(" + path + ");" +
                 "-fx-background-size: stretch;");
    }
}
