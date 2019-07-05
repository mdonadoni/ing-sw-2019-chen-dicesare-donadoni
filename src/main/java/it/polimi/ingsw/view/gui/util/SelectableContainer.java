package it.polimi.ingsw.view.gui.util;

/**
 * Interface for selectable container
 */
public interface SelectableContainer {
    /**
     * Find selectable by UUID
     * @param uuid UUID of the selectable
     * @return The selectable to find
     */
    Selectable findSelectable(String uuid);
}
