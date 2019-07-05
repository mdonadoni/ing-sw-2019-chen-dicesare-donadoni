package it.polimi.ingsw.view.gui.util;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;

/**
 * Represent stretch image
 */
public class StretchImage extends Pane {
    /**
     * The image
     */
    Image img;
    /**
     * the width of the image
     */
    double imageWidth = 0;
    /**
     * the height of the image
     */
    double imageHeight = 0;

    /**
     * Standarc Constructor
     */
    public StretchImage() {
    }

    /**
     * Constructor with an Image
     * @param image the image to set
     */
    public StretchImage(Image image) {
        setImage(image);
    }

    /**
     * Constructor with the path of the image
     * @param path the path of the image
     */
    public StretchImage(String path) {
        setImage(ImageManager.getResourceImage(path));
    }

    /**
     * Set image
     * @param img the image to set
     */
    private void setImage(Image img) {
        this.img = img;
        imageWidth = img.getWidth();
        imageHeight = img.getHeight();
        setPrefSize(imageWidth, imageHeight);
        setBackground(
            new Background(
                new BackgroundFill(
                    new ImagePattern(img),
                    CornerRadii.EMPTY,
                    Insets.EMPTY
                )
            )
        );
    }

    /**
     * get the width
     * @return the width
     */
    public double getImageWidth() {
        return imageWidth;
    }
    /**
     * get the height
     * @return the height
     */
    public double getImageHeight() {
        return imageHeight;
    }
}
