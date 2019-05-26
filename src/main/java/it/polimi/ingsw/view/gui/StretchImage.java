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

    public StretchImage(Image image) {
        setImage(image);
    }

    public StretchImage(String path) {
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            throw new ResourceException("Cannot find image resource");
        }
        Image image = new Image(stream);
        setImage(image);
    }

    private void setImage(Image img) {
        this.img = img;
        setPrefSize(img.getWidth(), img.getHeight());
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
        return img.getWidth();
    }

    public double getImageHeight() {
        return img.getHeight();
    }
}
