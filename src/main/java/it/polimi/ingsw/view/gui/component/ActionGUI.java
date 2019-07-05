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

/**
 * Represent the action in the GUI
 */
public class ActionGUI extends Button implements Selectable {
    /**
     * MiniAction to represent
     */
    private MiniAction action;

    /**
     * Constructor of the action
     * @param action the action to represent
     */
    public ActionGUI(MiniAction action) {
        this.action = action;
        setText(Descriptions.describe(action));
        setDisable(true);
    }

    /**
     * Get the UUID of the action
     * @return the UUID of the action
     */
    @Override
    public String getUuid() {
        return action.getUuid();
    }

    /**
     * Enable the action
     * @param notifyChange the change notifier
     */
    @Override
    public void enable(Runnable notifyChange) {
        setDisable(false);
        setOnAction(e -> notifyChange.run());
        setSelected(false);
    }

    /**
     * Set the action as selected
     * @param selected true as selected or false as not selected
     */
    @Override
    public void setSelected(boolean selected) {
        if (selected) {
            setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM)));
        } else {
            setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM)));
        }
    }

    /**
     * Disable the action
     */
    @Override
    public void disable() {
        setDisable(true);
        setBorder(null);
    }
}
