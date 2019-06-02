package it.polimi.ingsw.view.gui.util;

public interface Selectable {
    String getUuid();
    void enable(Runnable notifyChange);
    void setSelected(boolean selected);
    void disable();
}
