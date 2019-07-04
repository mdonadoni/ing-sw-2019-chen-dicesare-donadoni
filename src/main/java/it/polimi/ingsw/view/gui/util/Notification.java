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

public class Notification {
    private static final long GAP = 20;
    private static final long PAD = 10;
    private static final long WIDTH = 200;
    private static final long HEIGHT = 80;
    private static final Duration NOTIFICATION_DURATION = Duration.seconds(5);
    private static final Duration FADE_DURATION = Duration.seconds(0.5);

    private static List<Stage> visibleNotifications = new ArrayList<>();

    /**
     * This class should not be constructed
     */
    private Notification() {}

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

    private static void closeNotification(Stage popup) {
        popup.close();
        visibleNotifications.remove(popup);
        fixPositions();
    }

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
