package it.polimi.ingsw.view.gui.util;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.transform.Rotate;

import java.util.HashMap;
import java.util.Map;

public class Composition extends Region {

    private double compositionWidth;
    private double compositionHeight;
    private Map<Node, Position> nodeBounds = new HashMap<>();
    private boolean preserveRatio = false;


    public void add(Node node, double x, double y, double width, double height) {
        add(node, new Position(x, y, width, height, 0));
    }

    public void add(Node node, Position bounds) {
        getChildren().add(node);
        nodeBounds.put(node, bounds);
        node.getTransforms().add(new Rotate(bounds.getRotation()));
    }

    public void setPreserveRatio(boolean preserveRatio) {
        this.preserveRatio = preserveRatio;
        requestLayout();
    }

    public void setCompositionHeight(double compositionHeight) {
        this.compositionHeight = compositionHeight;
        requestLayout();
    }

    public void setCompositionWidth(double compositionWidth) {
        this.compositionWidth = compositionWidth;
        requestLayout();
    }

    @Override
    protected double computePrefWidth(double v) {
        return compositionWidth;
    }

    @Override
    protected double computePrefHeight(double v) {
        return compositionHeight;
    }

    @Override
    protected void layoutChildren() {
        getChildren().forEach(node -> {
            Position bounds = nodeBounds.get(node);
            double factorX = getWidth()/ compositionWidth;
            double factorY = getHeight()/ compositionHeight;
            if (preserveRatio) {
                double factor = Math.min(factorX, factorY);
                factorX = factor;
                factorY = factor;
            }
            layoutInArea(
                    node,
                    bounds.getX() * factorX,
                    bounds.getY() * factorY,
                    bounds.getWidth() * factorX,
                    bounds.getHeight() * factorY,
                    0,
                    HPos.LEFT,
                    VPos.TOP);
        });
    }
}
