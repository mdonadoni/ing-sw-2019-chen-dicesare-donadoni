package it.polimi.ingsw.view.gui.util;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Represent a selectable area
 */
public class SelectableArea extends Pane implements Selectable {
    /**
     * UUID of the Pane
     */
    String uuid;

    /**
     * Constructor of the class
     * @param uuid UUID of the Pane
     */
    public SelectableArea(String uuid) {
        this.uuid = uuid;
        setVisible(false);
    }

    /**
     * Get the UUID
     * @return the UUID
     */
    @Override
    public String getUuid() {
        return uuid;
    }

    /**
     * Enable the area
     * @param notifyChange the change notifier
     */
    @Override
    public void enable(Runnable notifyChange) {
        setVisible(true);
        setOnMouseClicked(e -> notifyChange.run());
        setSelected(false);
    }

    /**
     * Set the area as selected
     * @param selected true as selected or false as not selected
     */
    @Override
    public void setSelected(boolean selected) {
        Color color;
        if (selected) {
            color = Color.GREEN;
        } else {
            color = Color.YELLOW;
        }
        setBorder(new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM)));
    }

    /**
     * Disable the area
     */
    @Override
    public void disable() {
        setVisible(false);
        setBorder(null);
        setOnMouseClicked(null);
    }
}
