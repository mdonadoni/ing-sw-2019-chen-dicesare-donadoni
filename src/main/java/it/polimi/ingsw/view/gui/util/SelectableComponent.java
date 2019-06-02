package it.polimi.ingsw.view.gui.util;

import javafx.scene.Node;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;

public class SelectableComponent implements Selectable {
    Bloom bloom;
    InnerShadow innerShadow;
    Effect oldEffect;
    Node node;
    String uuid;

    public SelectableComponent(Node node, String uuid) {
        this.node = node;
        this.uuid = uuid;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

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

    @Override
    public void setSelected(boolean selected) {
        if (selected) {
            innerShadow.setColor(Color.GREEN);
        } else {
            innerShadow.setColor(Color.YELLOW);
        }
    }

    @Override
    public void disable() {
        node.setOnMouseEntered(null);
        node.setOnMouseExited(null);
        node.setOnMouseClicked(null);
        node.setEffect(oldEffect);
    }
}
