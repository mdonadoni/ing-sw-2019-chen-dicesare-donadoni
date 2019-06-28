package it.polimi.ingsw.view.gui.util;

import com.fasterxml.jackson.databind.JsonNode;

public class Position {
    private double x;
    private double y;
    private double width;
    private double height;
    private double rotation;

    public Position() {
        x = 0;
        y = 0;
        width = 0;
        height = 0;
        rotation = 0;
    }

    public Position(double x, double y, double width, double height, double rotation) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }

    public static Position fromJson(JsonNode json) {
        Position p = new Position();
        p.setX(json.get("x").asDouble());
        p.setY(json.get("y").asDouble());

        if (json.has("w")) {
            p.setWidth(json.get("w").asDouble());
        } else {
            p.setWidth(json.get("width").asDouble());
        }

        if (json.has("h")) {
            p.setHeight(json.get("h").asDouble());
        } else {
            p.setHeight(json.get("height").asDouble());
        }

        if (json.has("r")) {
            p.setRotation(json.get("r").asDouble());
        } else if (json.has("rotation")) {
            p.setRotation(json.get("rotation").asDouble());
        }

        return p;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getRotation() {
        return rotation;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
