package it.polimi.ingsw.view.gui.util;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

/**
 * FXML loader
 */
public class Loader {
    /**
     * This class should not be constructed.
     */
    private Loader() {}

    /**
     * Load the file fxml
     * @param path path of the file
     * @param owner the owner of the fxml
     */
    public static void load(String path, Object owner) {
        FXMLLoader fxmlLoader = new FXMLLoader(
                Loader.class.getResource(path));
        fxmlLoader.setRoot(owner);
        fxmlLoader.setController(owner);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
