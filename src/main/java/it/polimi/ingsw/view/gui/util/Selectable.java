package it.polimi.ingsw.view.gui.util;
/**
 * Interface of selectable object
 */
public interface Selectable {
    /**
     * Get the UUID of the object
     * @return The UUID of the object
     */
    String getUuid();

    /**
     * Enable the selectable
     * @param notifyChange the change notifier
     */
    void enable(Runnable notifyChange);
    /**
     * Set if is selected
     * @param selected true as selected or false as not selected
     */
    void setSelected(boolean selected);

    /**
     * Disable the object
     */
    void disable();
}
