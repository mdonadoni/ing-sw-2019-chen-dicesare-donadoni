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
        super.layoutChildren();
        double layoutWidth = Math.min(getWidth(), getHeight()/ contentHeight * contentWidth);
        double layoutHeight = Math.min(getHeight(), getWidth()/ contentWidth * contentHeight);
        getChildren().forEach((child) -> layoutInArea(child, (getWidth() - layoutWidth)/2, (getHeight()-layoutHeight)/2, layoutWidth, layoutHeight, 0, HPos.LEFT, VPos.TOP));
    }
}
