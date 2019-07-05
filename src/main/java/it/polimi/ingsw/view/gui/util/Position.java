package it.polimi.ingsw.view.gui.util;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Represent the position ef the components
 */
public class Position {
    /**
     * Coordinate x
     */
    private double x;
    /**
     * Coordinate y
     */
    private double y;
    /**
     * Width of the node
     */
    private double width;
    /**
     * Height of the node
     */
    private double height;
    /**
     * The rotation of the node
     */
    private double rotation;

    /**
     * Standard constructor of the class
     */
    public Position() {
        x = 0;
        y = 0;
        width = 0;
        height = 0;
        rotation = 0;
    }

    /**
     * Constructor of the class
     * @param x coordinate x
     * @param y coordinate y
     * @param width width dimension
     * @param height height dimension
     * @param rotation amount of rotation
     */
    public Position(double x, double y, double width, double height, double rotation) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }

    /**
     * Get position from json
     * @param json JsonNode to get the position
     * @return The position
     */
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

    /**
     * Set the width
     * @param width the width to set
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * set the height
     * @param height the height to set
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * set the rotation
     * @param rotation the rotation to set
     */
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
    /**
     * set the coordinate x
     * @param x the coordinate x to set
     */
    public void setX(double x) {
        this.x = x;
    }
    /**
     * set the coordinate x
     * @param y the coordinate x to set
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * get the width
     * @return the width
     */
    public double getWidth() {
        return width;
    }
    /**
     * get the height
     * @return the height
     */
    public double getHeight() {
        return height;
    }
    /**
     * get the rotation
     * @return the rotation
     */
    public double getRotation() {
        return rotation;
    }
    /**
     * get the coordinate x
     * @return the coordinate x
     */
    public double getX() {
        return x;
    }
    /**
     * get the coordinate y
     * @return the coordinate y
     */
    public double getY() {
        return y;
    }
}
