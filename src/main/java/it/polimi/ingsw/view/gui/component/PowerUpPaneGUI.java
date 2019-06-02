package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.PowerUp;
import it.polimi.ingsw.view.gui.util.Selectable;
import it.polimi.ingsw.view.gui.util.SelectableContainer;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.List;

public class PowerUpPaneGUI extends GridPane implements SelectableContainer {
    private static final double PAD = 3;
    private static final int MAX_POWERUP = 4;

    private List<PowerUpGUI> powerupsGUI = new ArrayList<>();

    public PowerUpPaneGUI(List<PowerUp> powerups) {
        // There are MAX_POWERUP+1 pad columns
        ColumnConstraints padCol = new ColumnConstraints();
        padCol.setPercentWidth(PAD);
        ColumnConstraints powerupCol = new ColumnConstraints();
        powerupCol.setPercentWidth((100.0 - (MAX_POWERUP+1)*PAD)/MAX_POWERUP);

        for (int i = 0; i < MAX_POWERUP; i++) {
            getColumnConstraints().add(padCol);
            getColumnConstraints().add(powerupCol);
        }
        getColumnConstraints().add(padCol);

        RowConstraints row = new RowConstraints();
        row.setPercentHeight(100);
        getRowConstraints().add(row);

        for (int i = 0; i < powerups.size(); i++) {
            PowerUpGUI powerupGUI = new PowerUpGUI(powerups.get(i));
            powerupsGUI.add(powerupGUI);
            add(powerupGUI, 1+i*2, 0);
        }
    }

    @Override
    public Selectable findSelectable(String uuid) {
        for (PowerUpGUI p : powerupsGUI) {
            if (p.getUuid().equals(uuid)) {
                return p;
            }
        }
        return null;
    }
}
