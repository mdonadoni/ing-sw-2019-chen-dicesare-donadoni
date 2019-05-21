package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.minified.MiniGameBoard;
import it.polimi.ingsw.model.minified.MiniMatch;
import it.polimi.ingsw.view.gui.GameBoardGUI;
import it.polimi.ingsw.view.gui.LoginPane;
import it.polimi.ingsw.view.gui.WaitingPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Arrays;

public class ViewGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Drawing Operations Test");

        // Inizio prova board
        Match match = new Match(Arrays.asList("a", "b", "c"), BoardType.SMALL);
        match.getGameBoard().getBoard().getStandardSquare(new Coordinate(1, 2)).setAmmoTile(new AmmoTile(AmmoColor.BLUE, AmmoColor.YELLOW, AmmoColor.BLUE));
        MiniMatch miniMatch = new MiniMatch(match);

        MiniGameBoard board = miniMatch.getGameBoard();

        GameBoardGUI gameBoardGUI = new GameBoardGUI(board);
        gameBoardGUI.getStylesheets().add("/gui/css/stylesheet.css");
        primaryStage.setScene(new Scene(gameBoardGUI, 600, 400));
        // Fine prova board

        LoginPane login = new LoginPane();

        login.connectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                primaryStage.setScene(new Scene(new WaitingPane(), 800, 600));
            }
        });

        //primaryStage.setScene(new Scene(login));

        primaryStage.setMinWidth(854);
        primaryStage.setMinHeight(480);
        primaryStage.show();
    }
}