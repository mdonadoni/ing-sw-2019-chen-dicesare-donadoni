package it.polimi.ingsw.view.gui.util;

import javafx.scene.Node;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;

/**
 * Represent a selectable component
 */
public class SelectableComponent implements Selectable {
    /**
     * Bloom effect
     */
    Bloom bloom;
    /**
     * Inner shadow effect
     */
    InnerShadow innerShadow;
    /**
     * Old effect
     */
    Effect oldEffect;
    /**
     * Node with the effect
     */
    Node node;
    /**
     * UUID of the component
     */
    String uuid;

    /**
     * Constructor of the class
     * @param node the node with effect
     * @param uuid the UUID of the node
     */
    public SelectableComponent(Node node, String uuid) {
        this.node = node;
        this.uuid = uuid;
    }

    /**
     * Get the UUID of the component
     * @return UUID of the component
     */
    @Override
    public String getUuid() {
        return uuid;
    }

    /**
     * Enable the component
     * @param notifyChange the change notifier
     */
    @Override
    public void enable(Runnable notifyChange) {
        oldEffect = node.getEffect();

        innerShadow = new InnerShadow();
        innerShadow.setInput(oldEffect);

        bloom = new Bloom();
        bloom.setInput(innerShadow);

        node.setOnMouseEntered(e -> node.setEffect(bloom));
        node.setOnMouseExited(e -> node.setEffect(innerShadow));
        node.setOnMouseClicked(e -> notifyChange.run());

        setSelected(false);
        node.setEffect(innerShadow);
    }

    /**
     * Set the component as selected
     * @param selected true as selected or false as not selected
     */
    @Override
    public void setSelected(boolean selected) {
        if (selected) {
            innerShadow.setColor(Color.GREEN);
        } else {
            innerShadow.setColor(Color.YELLOW);
        }
    }

    /**
     * Disable the component
     */
    @Override
    public void disable() {
        node.setOnMouseEntered(null);
        node.setOnMouseExited(null);
        node.setOnMouseClicked(null);
        node.setEffect(oldEffect);
    }
}
