package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.view.gui.util.Loader;
import it.polimi.ingsw.view.gui.util.Notification;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class LoginPane extends GridPane {

    private static String SOCKET = "Socket";
    private static String RMI = "RMI";

    @FXML
    private TextField serverField;
    @FXML
    private ChoiceBox<String> protocolBox;
    @FXML
    private TextField portField;
    @FXML
    private TextField nicknameField;
    @FXML
    private Button loginButton;

    private BiConsumer<LoginInfo, Consumer<LoginResult>> loginCallback;

    private BooleanProperty connected;

    public LoginPane() {
        Loader.load("/gui/fxml/LoginPane.fxml", this);

        connected = new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize() {
        protocolBox.getItems().addAll(SOCKET, RMI);
        protocolBox.setValue(SOCKET);
    }

    @FXML
    private void handleLoginClick() {
        // Disable login button
        loginButton.setDisable(true);

        // Find port
        int port;
        try {
            port = Integer.parseInt(portField.getText());
        } catch (Exception e) {
            Notification.newNotification("La porta deve essere un numero");
            loginButton.setDisable(false);
            return;
        }
         // Find protocolBox
        ConnectionType type =
                protocolBox.getSelectionModel().getSelectedItem().equals(SOCKET) ?
                        ConnectionType.SOCKET : ConnectionType.RMI;

        // Create Login info
        LoginInfo info = new LoginInfo(
                serverField.getText(),
                port,
                type,
                nicknameField.getText()
        );

        new Thread(() -> {
            loginCallback.accept(info, r ->
                Platform.runLater(() -> {
                    switch (r) {
                        case CONNECTION_ERROR:
                            Notification.newNotification("Non riesco a connettermi al server");
                            loginButton.setDisable(false);
                            break;
                        case NICKNAME_NOT_VALID:
                            Notification.newNotification("Nickname non valido o già utilizzato");
                            loginButton.setDisable(false);
                            break;
                        case LOGIN_SUCCESSFUL:
                            connected.setValue(true);
                            break;
                    }
                })
            );
        }).start();
    }

    public ReadOnlyBooleanProperty connectedProperty() {
        return connected;
    }

    public void setLoginCallback(BiConsumer<LoginInfo, Consumer<LoginResult>> loginCallback) {
        this.loginCallback = loginCallback;
    }
}
