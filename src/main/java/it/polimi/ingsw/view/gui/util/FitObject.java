package it.polimi.ingsw.view.gui.util;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Region;

/**
 * Define a content
 */
public class FitObject extends Region {
    /**
     * the width of the content
     */
    private double contentWidth = 500;
    /**
     * the height of the content
     */
    private double contentHeight = 300;

    /**
     * Get the list of nodes
     * @return the list of nodes
     */
    @Override
    public ObservableList<Node> getChildren() {
        return super.getChildren();
    }

    /**
     * Return the content width
     * @param h not used
     * @return the content width
     */
    @Override
    protected double computePrefWidth(double h) {
        return contentWidth;
    }

    /**
     * Return the content height
     * @param w not used
     * @return the content height
     */
    @Override
    protected double computePrefHeight(double w) {
        return contentHeight;
    }

    /**
     * Set the width
     * @param width the width to set
     */
    public void setContentWidth(double width) {
        this.contentWidth = width;
        requestParentLayout();
    }

    /**
     * Set the height
     * @param height the height to set
     */
    public void setContentHeight(double height) {
        this.contentHeight = height;
        requestParentLayout();
    }

    /**
     * Define the layout of the children in the content
     */
    @Override
    protected void layoutChildren() {
        double left = getInsets().getLeft();
        double right = getInsets().getRight();
        double top = getInsets().getTop();
        double bottom = getInsets().getBottom();
        double realWidth = getWidth() - left - right;
        double realHeight = getHeight() - top- bottom;
        double layoutWidth = Math.min(realWidth, realHeight/ contentHeight * contentWidth);
        double layoutHeight = Math.min(realHeight, realWidth/ contentWidth * contentHeight);
        getChildren().forEach(child -> layoutInArea(child, left + (realWidth - layoutWidth)/2, top + (realHeight -layoutHeight)/2, layoutWidth, layoutHeight, 0, HPos.LEFT, VPos.TOP));
    }
}
