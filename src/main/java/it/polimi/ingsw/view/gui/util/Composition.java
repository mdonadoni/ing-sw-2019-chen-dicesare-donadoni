package it.polimi.ingsw.view.gui.util;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.transform.Rotate;

import java.util.HashMap;
import java.util.Map;

/**
 * Composition of the different part of the gui
 */
public class Composition extends Region {
    /**
     * width of the composition
     */
    private double compositionWidth;
    /**
     * height of the composition
     */
    private double compositionHeight;
    /**
     * Map of the node and it's position
     */
    private Map<Node, Position> nodeBounds = new HashMap<>();
    /**
     * The composition doesn't preserve its ratio
     */
    private boolean preserveRatio = false;

    /**
     * Add a node in the composition
     * @param node Node to add
     * @param x coordinate x of the node
     * @param y coordinate y of the node
     * @param width width of the node
     * @param height height of the node
     */
    public void add(Node node, double x, double y, double width, double height) {
        add(node, new Position(x, y, width, height, 0));
    }

    /**
     * Add a node in the composition
     * @param node Node to add
     * @param bounds the bounds of the node
     */
    public void add(Node node, Position bounds) {
        getChildren().add(node);
        nodeBounds.put(node, bounds);
        node.getTransforms().add(new Rotate(bounds.getRotation()));
    }

    /**
     * Set preserve ratio
     * @param preserveRatio if the ratio needs to be preserved or not
     */
    public void setPreserveRatio(boolean preserveRatio) {
        this.preserveRatio = preserveRatio;
        requestLayout();
    }
    /**
     * Set the height
     * @param compositionHeight the height
     */
    public void setCompositionHeight(double compositionHeight) {
        this.compositionHeight = compositionHeight;
        requestLayout();
    }
    /**
     * Set the width
     * @param compositionWidth compositionWidth the width
     */
    public void setCompositionWidth(double compositionWidth) {
        this.compositionWidth = compositionWidth;
        requestLayout();
    }

    /**
     * Return the predefined width of the composition
     * @param v not used
     * @return the predefined width of the composition
     */
    @Override
    protected double computePrefWidth(double v) {
        return compositionWidth;
    }

    /**
     * Return the predefined height of the composition
     * @param v not used
     * @return the predefined height of the composition
     */
    @Override
    protected double computePrefHeight(double v) {
        return compositionHeight;
    }

    /**
     * Define the layout of the node in the composition
     */
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
