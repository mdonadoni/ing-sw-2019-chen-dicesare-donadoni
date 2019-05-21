package it.polimi.ingsw.view.gui;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class Loader {
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
