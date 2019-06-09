package it.polimi.ingsw.view.gui.util;

import it.polimi.ingsw.util.ResourceManager;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;

public class StretchImage extends Pane {
    Image img;
    double imageWidth = 0;
    double imageHeight = 0;


    public StretchImage() {
    }

    public StretchImage(Image image) {
        setImage(image);
    }

    public StretchImage(String path) {
        setImage(new Image(ResourceManager.get(path)));
    }

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

    public double getImageWidth() {
        return imageWidth;
    }

    public double getImageHeight() {
        return imageHeight;
    }
}
