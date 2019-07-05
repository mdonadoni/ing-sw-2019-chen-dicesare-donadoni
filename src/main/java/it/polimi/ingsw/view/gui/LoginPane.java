package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.view.gui.util.ImageManager;
import it.polimi.ingsw.view.gui.util.Loader;
import it.polimi.ingsw.view.gui.util.Notification;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
/**
 * Pane for the login
 */
public class LoginPane extends GridPane {

    private static final String SOCKET = "Socket";
    private static final String RMI = "RMI";
    private static final String BACKGROUND_PATH = "/gui/loginBackground.jpg";
    /**
     * text field for the address server
     */
    @FXML
    private TextField serverField;
    /**
     * choice box for the type of protocol
     */
    @FXML
    private ChoiceBox<String> protocolBox;
    /**
     * text field for the port
     */
    @FXML
    private TextField portField;
    /**
     * text field for the nickname
     */
    @FXML
    private TextField nicknameField;
    /**
     * Login button
     */
    @FXML
    private Button loginButton;
    /**
     * Login call back
     */
    private BiConsumer<LoginInfo, Consumer<LoginResult>> loginCallback;
    /**
     * Connected property
     */
    private BooleanProperty connected;
    /**
     * Constructor, load the pane from file
     */
    public LoginPane() {
        Loader.load("/gui/fxml/LoginPane.fxml", this);

        connected = new SimpleBooleanProperty(false);

        // Set the background
        ImagePattern imagePattern = new ImagePattern(ImageManager.getResourceImage(BACKGROUND_PATH));
        BackgroundFill backgroundFill = new BackgroundFill(imagePattern, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        setBackground(background);
    }
    /**
     * Initialize the pane
     */
    @FXML
    private void initialize() {
        protocolBox.getItems().addAll(SOCKET, RMI);
        protocolBox.setValue(SOCKET);
    }
    /**
     * Handle the login button
     */
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

        new Thread(() ->
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
            )
        ).start();
    }
    /**
     * Get the connection property
     * @return the boolean property of the connection
     */
    public ReadOnlyBooleanProperty connectedProperty() {
        return connected;
    }
    /**
     * Set the login callback
     * @param loginCallback the login callback to set
     */
    public void setLoginCallback(BiConsumer<LoginInfo, Consumer<LoginResult>> loginCallback) {
        this.loginCallback = loginCallback;
    }
}
