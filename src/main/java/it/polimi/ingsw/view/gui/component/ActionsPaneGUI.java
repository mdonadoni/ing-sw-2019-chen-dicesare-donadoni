package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.minified.MiniAction;
import it.polimi.ingsw.view.gui.util.Selectable;
import it.polimi.ingsw.view.gui.util.SelectableContainer;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ActionsPaneGUI extends VBox  implements SelectableContainer {
    private List<ActionGUI> actions = new ArrayList<>();

    public ActionsPaneGUI(List<MiniAction> available) {
        available.forEach(action -> actions.add(new ActionGUI(action)));
        getChildren().addAll(actions);
    }

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
