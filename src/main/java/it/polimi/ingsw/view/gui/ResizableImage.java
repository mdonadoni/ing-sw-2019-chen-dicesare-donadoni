package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.util.ResourceException;
import javafx.scene.image.Image;

import java.io.InputStream;

public class ResizableImage extends FitObject {
    StretchImage stretchImage;

    public ResizableImage() {

    }

    public ResizableImage(String path) {
        loadImage(path);
    }

    public ResizableImage(Image img) {
        setImage(img);
    }

    public void setImage(Image img) {
        stretchImage = new StretchImage(img);
        setContentWidth(stretchImage.getImageWidth());
        setContentHeight(stretchImage.getImageHeight());
        getChildren().clear();
        getChildren().add(stretchImage);
    }

    public void loadImage(String path) {
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            throw new ResourceException("Cannot find image resource");
        }
        setImage(new Image(stream));
    }
}
