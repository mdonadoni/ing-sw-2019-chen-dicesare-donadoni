package it.polimi.ingsw.view.gui.util;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Notification for the gui
 */
public class Notification {
    /**
     * the dimension of the gap
     */
    private static final long GAP = 20;
    /**
     * dimension of the pad
     */
    private static final long PAD = 10;
    /**
     * dimension of the width
     */
    private static final long WIDTH = 200;
    /**
     * dimension of the height
     */
    private static final long HEIGHT = 80;
    /**
     * Duration of the notification
     */
    private static final Duration NOTIFICATION_DURATION = Duration.seconds(5);
    /**
     * Duration of the fade
     */
    private static final Duration FADE_DURATION = Duration.seconds(0.5);
    /**
     * List of visible notification
     */
    private static List<Stage> visibleNotifications = new ArrayList<>();

    /**
     * This class should not be constructed
     */
    private Notification() {}

    /**
     * Create a new notification
     * @param message the message of the notification
     */
    public static void newNotification(String message) {
        // Create new window
        Stage popup = new Stage(StageStyle.TRANSPARENT);
        popup.setAlwaysOnTop(true);
        popup.setResizable(false);

        // Create scene
        Text text = new Text(message);
        TextFlow textFlow = new TextFlow(text);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(PAD, PAD, PAD, PAD));
        grid.getChildren().add(textFlow);

        Scene scene = new Scene(grid, WIDTH, HEIGHT);
        scene.setFill(Color.TRANSPARENT);
        scene.setOnMouseClicked(e -> closeNotification(popup));
        popup.setScene(scene);

        // Create animation
        PauseTransition delay = new PauseTransition(NOTIFICATION_DURATION);
        FadeTransition fadeOut = new FadeTransition(FADE_DURATION, scene.getRoot());
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> closeNotification(popup));
        SequentialTransition sequence = new SequentialTransition(delay, fadeOut);

        // Add to visibile notifications
        visibleNotifications.add(popup);
        fixPositions();

        // Play
        sequence.play();
        popup.show();
    }

    /**
     * Close the notification
     * @param popup the popup to close
     */
    private static void closeNotification(Stage popup) {
        popup.close();
        visibleNotifications.remove(popup);
        fixPositions();
    }

    /**
     * Fix position on the screen of the notification
     */
    private static void fixPositions() {
        double screenX = Screen.getPrimary().getBounds().getMinX();
        double screenY = Screen.getPrimary().getBounds().getMinY();

        for (int i = 0; i < visibleNotifications.size(); i++) {
            Stage popup = visibleNotifications.get(i);
            popup.setX(screenX + GAP);
            popup.setY(screenY + GAP + (HEIGHT + GAP) * i);
        }
    }
}
