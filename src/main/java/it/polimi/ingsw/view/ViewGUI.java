package it.polimi.ingsw.view;

import it.polimi.ingsw.view.gui.LoginPane;
import it.polimi.ingsw.view.gui.ScalableGroup;
import it.polimi.ingsw.view.gui.WaitingPane;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ViewGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Drawing Operations Test");

        // Inizio prova board
        Image img = new Image(ViewGUI.class.getResourceAsStream("/gui/boards/small.png"));
        ImageView imgView = new ImageView(img);

        Rectangle rect = new Rectangle(410, 398, 848-410, 803-398);
        rect.setStroke(Color.BLUE);
        rect.setStrokeWidth(20);

        Group g = new Group();
        g.getChildren().addAll(imgView, rect);

        ScalableGroup sg = new ScalableGroup();
        sg.setContent(g);

        GridPane p = new GridPane();
        p.getChildren().add(sg);
        // Fine prova board

        LoginPane login = new LoginPane();

        login.connectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                primaryStage.setScene(new Scene(new WaitingPane(), 800, 600));
            }
        });

        primaryStage.setScene(new Scene(login));

        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(300);
        primaryStage.show();
    }
}