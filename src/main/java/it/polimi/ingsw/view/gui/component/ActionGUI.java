package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.minified.MiniAction;
import it.polimi.ingsw.view.Descriptions;
import it.polimi.ingsw.view.gui.util.Selectable;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class ActionGUI extends Button implements Selectable {
    private MiniAction action;
    public ActionGUI(MiniAction action) {
        this.action = action;
        setText(Descriptions.describe(action));
        setDisable(true);
    }

    @Override
    public String getUuid() {
        return action.getUuid();
    }

    @Override
    public void enable(Runnable notifyChange) {
        setDisable(false);
        setOnAction(e -> notifyChange.run());
        setSelected(false);
    }

    @Override
    public void setSelected(boolean selected) {
        if (selected) {
            setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM)));
        } else {
            setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM)));
        }
    }

    @Override
    public void disable() {
        setDisable(true);
        setBorder(null);
    }
}
