package it.polimi.ingsw.view.gui.util;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Region;

public class FitObject extends Region {

    private double contentWidth = 500;
    private double contentHeight = 300;

    @Override
    public ObservableList<Node> getChildren() {
        return super.getChildren();
    }

    @Override
    protected double computePrefWidth(double h) {
        return contentWidth;
    }

    @Override
    protected double computePrefHeight(double w) {
        return contentHeight;
    }

    public void setContentWidth(double width) {
        this.contentWidth = width;
        requestParentLayout();
    }

    public void setContentHeight(double height) {
        this.contentHeight = height;
        requestParentLayout();
    }

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
        getChildren().forEach((child) -> layoutInArea(child, left + (realWidth - layoutWidth)/2, top + (realHeight -layoutHeight)/2, layoutWidth, layoutHeight, 0, HPos.LEFT, VPos.TOP));
    }
}
