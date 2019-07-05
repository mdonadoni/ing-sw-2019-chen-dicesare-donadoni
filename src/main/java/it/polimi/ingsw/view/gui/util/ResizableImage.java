package it.polimi.ingsw.view.gui.util;

import javafx.scene.image.Image;

/**
 * Defines a resizable image
 */
public class ResizableImage extends FitObject {
    /**
     * the StretchImage
     */
    StretchImage stretchImage = null;

    /**
     * Constructor of the class
     */
    public ResizableImage() {

    }

    /**
     * Construct stretch image from a path
     * @param path the path of the image
     */
    public ResizableImage(String path) {
        setImage(path);
    }

    /**
     * Construct stretch image from a image
     * @param img the image to turn to stretch image
     */
    public ResizableImage(Image img) {
        setImage(img);
    }

    /**
     * Set image
     * @param img the image to set
     */
    public void setImage(Image img) {
        if (stretchImage != null) {
            getChildren().remove(stretchImage);
        }

        stretchImage = new StretchImage(img);
        setContentWidth(stretchImage.getImageWidth());
        setContentHeight(stretchImage.getImageHeight());
        getChildren().add(0, stretchImage);
    }

    /**
     * Set the image
     * @param path the path of the image
     */
    public void setImage(String path) {
        setImage(ImageManager.getResourceImage(path));
    }

    /**
     * Get the image
     * @return the stretchImage
     */
    public StretchImage getImage() {
        return stretchImage;
    }
}
