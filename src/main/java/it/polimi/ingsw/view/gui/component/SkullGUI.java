package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.view.gui.util.GridUtils;
import it.polimi.ingsw.view.gui.util.StretchImage;
import javafx.scene.layout.GridPane;

public class SkullGUI extends GridPane {

    SkullGUI(){
        GridUtils.setPercentColumns(this, 25, 50, 25);
        GridUtils.setPercentRows(this, 25, 50, 25);

        add(new StretchImage("/gui/players/marks/skull.png"), 1, 1);
    }

}
