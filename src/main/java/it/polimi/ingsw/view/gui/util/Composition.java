package it.polimi.ingsw.view.gui.util;

import javafx.geometry.BoundingBox;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Region;

import java.util.HashMap;
import java.util.Map;

public class Composition extends Region {

    private double compositionWidth;
    private double compositionHeight;
    private Map<Node, BoundingBox> nodeBounds = new HashMap<>();
    private boolean preserveRatio = false;


    public void add(Node node, double x, double y, double widht, double height) {
        add(node, new BoundingBox(x, y, 0, widht, height, 0));
    }

    public void add(Node node, BoundingBox bounds) {
        getChildren().add(node);
        nodeBounds.put(node, bounds);
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
            BoundingBox bounds = nodeBounds.get(node);
            double factorX = getWidth()/ compositionWidth;
            double factorY = getHeight()/ compositionHeight;
            if (preserveRatio) {
                double factor = Math.min(factorX, factorY);
                factorX = factor;
                factorY = factor;
            }
            layoutInArea(
                    node,
                    bounds.getMinX() * factorX,
                    bounds.getMinY() * factorY,
                    bounds.getWidth() * factorX,
                    bounds.getHeight() * factorY,
                    0,
                    HPos.LEFT,
                    VPos.TOP);
        });
    }
}
