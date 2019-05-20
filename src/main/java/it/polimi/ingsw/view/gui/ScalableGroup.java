package it.polimi.ingsw.view.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Group;
import javafx.scene.layout.Region;
import javafx.scene.transform.Scale;

public class ScalableGroup extends Region {
    ObjectProperty<Group> content = new SimpleObjectProperty<>();
    Scale scaleTransform;

    public ScalableGroup() {
        this(null);
    }

    public ScalableGroup(Group group) {
        content.setValue(group);

        content.addListener((obs, oldValue, newValue) -> {
            if (oldValue != null) {
                getChildren().remove(oldValue);
                oldValue.getTransforms().remove(scaleTransform);
            }

            if (newValue != null) {
                getChildren().add(newValue);

                scaleTransform = new Scale(1.0, 1.0, 0, 0);
                newValue.getTransforms().add(scaleTransform);

                setPrefSize(newValue.prefWidth(0), newValue.prefHeight(0));
            }
        });

        widthProperty().addListener((obs, oldValue, newValue) -> {
            refresh();
        });
        heightProperty().addListener((obs, oldValue, newValue) -> {
            refresh();
        });
    }

    private void refresh() {
        double factor = Math.min(
                getWidth() / content.getValue().prefWidth(0),
                getHeight() / content.getValue().prefHeight(0));
        scaleTransform.setX(factor);
        scaleTransform.setY(factor);
    }

    public void setContent(Group group) {
        content.setValue(group);
    }

    public ObjectProperty<Group> contentProperty() {
        return content;
    }
}
