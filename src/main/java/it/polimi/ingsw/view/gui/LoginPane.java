package it.polimi.ingsw.view.gui;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class LoginPane extends GridPane {
    @FXML
    private TextField server;
    @FXML
    private ChoiceBox<String> protocol;
    @FXML
    private TextField port;
    @FXML
    private TextField nickname;
    @FXML
    private Button btnLogin;

    private BooleanProperty connected;

    public LoginPane() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/gui/fxml/LoginPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        connected = new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize() {
        protocol.getItems().addAll("Socket", "RMI");
        protocol.setValue("Socket");
    }

    @FXML
    private void handleLoginClick() {
        btnLogin.setDisable(true);
        new Thread(() -> {
            try {
                //TODO implement connection
                Platform.runLater(() -> connected.setValue(true));
            }catch (Exception e) {
                Platform.runLater(() -> btnLogin.setDisable(false));
            }
        }).start();
    }

    public ReadOnlyBooleanProperty connectedProperty() {
        return connected;
    }

}
