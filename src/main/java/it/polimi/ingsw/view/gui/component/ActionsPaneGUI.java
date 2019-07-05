package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.minified.MiniAction;
import it.polimi.ingsw.view.gui.util.Selectable;
import it.polimi.ingsw.view.gui.util.SelectableContainer;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent the Pane for the action
 */
public class ActionsPaneGUI extends VBox  implements SelectableContainer {
    /**
     * List of ActionGUI
     */
    private List<ActionGUI> actions = new ArrayList<>();

    /**
     * Constructor of the class
     * @param available List of available action
     */
    public ActionsPaneGUI(List<MiniAction> available) {
        available.forEach(action -> actions.add(new ActionGUI(action)));
        getChildren().addAll(actions);
    }

    /**
     * Find selectable by UUID
     * @param uuid UUID of the selectable
     * @return the selectable pane
     */
    @Override
    public Selectable findSelectable(String uuid) {
        for (ActionGUI action : actions) {
            if (action.getUuid().equals(uuid)) {
                return action;
            }
        }
        return null;
    }
}
