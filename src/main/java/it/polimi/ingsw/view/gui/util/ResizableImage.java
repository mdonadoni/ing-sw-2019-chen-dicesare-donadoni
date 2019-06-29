package it.polimi.ingsw.view.gui.util;

import javafx.scene.image.Image;

public class ResizableImage extends FitObject {
    StretchImage stretchImage;

    public ResizableImage() {

    }

    public ResizableImage(String path) {
        setImage(path);
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

    public void setImage(String path) {
        setImage(ImageManager.getResourceImage(path));
    }

    public StretchImage getImage() {
        return stretchImage;
    }
}
