package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.minified.MiniGameBoard;
import it.polimi.ingsw.model.minified.MiniMatch;
import it.polimi.ingsw.model.minified.MiniPlayer;
import it.polimi.ingsw.view.gui.GameBoardGUI;
import it.polimi.ingsw.view.gui.LoginPane;
import it.polimi.ingsw.view.gui.PlayerBoardGUI;
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
        //match.getGameBoard().getBoard().getStandardSquare(new Coordinate(1, 2)).setAmmoTile(new AmmoTile(AmmoColor.BLUE, AmmoColor.YELLOW, AmmoColor.BLUE));
        match.getGameBoard().refillAmmoTile();
        MiniMatch miniMatch = new MiniMatch(match);

        MiniGameBoard board = miniMatch.getGameBoard();

        GameBoardGUI gameBoardGUI = new GameBoardGUI(board);
        gameBoardGUI.getStylesheets().add("/gui/css/stylesheet.css");
        //primaryStage.setScene(new Scene(gameBoardGUI, 600, 400));
        // Fine prova board

        LoginPane login = new LoginPane();

        login.connectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                primaryStage.setScene(new Scene(new WaitingPane(), 800, 600));
            }
        });

        //primaryStage.setScene(new Scene(login));

        Player player = new Player("thatDc", PlayerToken.PURPLE);
        player.addDamage(PlayerToken.PURPLE, 2);
        player.addDamage(PlayerToken.BLUE, 2);
        player.addDamage(PlayerToken.GREEN, 3);
        player.addDamage(PlayerToken.YELLOW, 1);
        player.setSkulls(3);
        player.addMark(PlayerToken.YELLOW, 3);
        player.addMark(PlayerToken.BLUE, 3);
        player.addMark(PlayerToken.PURPLE, 3);
        player.addMark(PlayerToken.GREEN, 3);
        MiniPlayer miniPlayer = new MiniPlayer(player);


        PlayerBoardGUI phb = new PlayerBoardGUI(miniPlayer);
        primaryStage.setScene(new Scene(phb, 600, 400));
        primaryStage.show();
        primaryStage.setMinWidth(854);
        primaryStage.setMinHeight(480);

    }
}