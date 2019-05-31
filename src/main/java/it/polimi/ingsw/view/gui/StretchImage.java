package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.util.ResourceException;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;

import java.io.InputStream;

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
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            throw new ResourceException("Cannot find image resource");
        }
        setImage(new Image(stream));
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
