package it.polimi.ingsw.view.gui.util;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class SelectableArea extends Pane implements Selectable {
    String uuid;
    public SelectableArea(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void enable(Runnable notifyChange) {
        setOnMouseClicked(e -> notifyChange.run());
        setSelected(false);
    }

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

    @Override
    public void disable() {
        setBorder(null);
        setOnMouseClicked(null);
    }
}