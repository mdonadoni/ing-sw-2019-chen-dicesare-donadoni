package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.minified.MiniGameBoard;
import it.polimi.ingsw.model.minified.MiniMatch;
import it.polimi.ingsw.model.minified.MiniModel;
import it.polimi.ingsw.model.minified.MiniPlayer;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.view.gui.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Drawing Operations Test");

        // Inizio prova board
        /*Match match = new Match(Arrays.asList("a", "b", "c"), BoardType.SMALL);
        match.getGameBoard().getBoard().getStandardSquare(new Coordinate(1, 2)).setAmmoTile(new AmmoTile(AmmoColor.BLUE, AmmoColor.YELLOW, AmmoColor.BLUE));
        match.getGameBoard().refillAmmoTile();
        MiniMatch miniMatch = new MiniMatch(match);

        MiniGameBoard board = miniMatch.getGameBoard();

        GameBoardGUI gameBoardGUI = new GameBoardGUI(board);
        gameBoardGUI.getStylesheets().add("/gui/css/stylesheet.css");
        //primaryStage.setScene(new Scene(gameBoardGUI, 600, 400));
        // Fine prova board
        */
        LoginPane login = new LoginPane();

        login.connectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                primaryStage.setScene(new Scene(new WaitingPane(), 800, 600));
            }
        });

        //primaryStage.setScene(new Scene(login));

        List<String> nicks = new ArrayList<>();
        nicks.add("thatDc");
        nicks.add("Mark03");
        nicks.add("Simone");
        nicks.add("Fuljo");
        nicks.add("Ricky");

        Match match = new Match(nicks, BoardType.SMALL);
        match.getGameBoard().refillAmmoTile();

        Player tempPlayer = match.getPlayerByNickname("thatDc");
        tempPlayer.addDamage(PlayerToken.PURPLE, 2);
        tempPlayer.addDamage(PlayerToken.BLUE, 2);
        tempPlayer.addDamage(PlayerToken.GREEN, 3);
        tempPlayer.addDamage(PlayerToken.YELLOW, 1);
        tempPlayer.setSkulls(3);
        tempPlayer.addMark(PlayerToken.YELLOW, 3);
        tempPlayer.addMark(PlayerToken.BLUE, 3);
        tempPlayer.addMark(PlayerToken.PURPLE, 3);
        tempPlayer.addMark(PlayerToken.GREEN, 3);
        tempPlayer.addAmmo(AmmoColor.BLUE);
        tempPlayer.addAmmo(AmmoColor.RED);
        tempPlayer.addAmmo(AmmoColor.YELLOW);
        tempPlayer.addAmmo(AmmoColor.YELLOW);
        tempPlayer.addAmmo(AmmoColor.RED);
        tempPlayer.grabWeapon(new Weapon("cyberblade"));
        tempPlayer.grabWeapon(new Weapon("rocketlauncher"));
        tempPlayer.grabWeapon(new Weapon("sledgehammer"));

        tempPlayer = match.getPlayerByNickname("Mark03");
        tempPlayer.addDamage(PlayerToken.GREY, 2);
        tempPlayer.addMark(PlayerToken.BLUE, 1);
        tempPlayer.setSkulls(1);
        tempPlayer.grabWeapon(new Weapon("lockrifle"));

        tempPlayer = match.getPlayerByNickname("Simone");
        tempPlayer.addDamage(PlayerToken.YELLOW, 2);
        tempPlayer.addDamage(PlayerToken.BLUE, 3);
        tempPlayer.addMark(PlayerToken.PURPLE, 1);
        tempPlayer.addMark(PlayerToken.GREY, 2);
        tempPlayer.setSkulls(0);
        tempPlayer.addAmmo(AmmoColor.YELLOW);
        tempPlayer.addAmmo(AmmoColor.YELLOW);
        tempPlayer.addAmmo(AmmoColor.RED);

        tempPlayer = match.getPlayerByNickname("Fuljo");
        tempPlayer.addDamage(PlayerToken.GREY, 3);
        tempPlayer.addMark(PlayerToken.GREEN, 2);
        tempPlayer.setSkulls(2);
        tempPlayer.grabWeapon(new Weapon("tractorbeam"));
        tempPlayer.grabWeapon(new Weapon("hellion"));

        tempPlayer = match.getPlayerByNickname("Ricky");
        tempPlayer.addDamage(PlayerToken.YELLOW, 2);
        tempPlayer.addMark(PlayerToken.BLUE, 1);
        tempPlayer.setSkulls(0);
        tempPlayer.addAmmo(AmmoColor.BLUE);
        tempPlayer.addAmmo(AmmoColor.RED);

        MiniModel miniModel = new MiniModel(match, match.getPlayerByNickname("thatDc"));

        primaryStage.setScene(new Scene(new UserViewGUI(miniModel), 600, 400));
        primaryStage.show();
        primaryStage.setMinWidth(854);
        primaryStage.setMinHeight(480);


        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final int p = i;
                Platform.runLater(() -> {
                    Notification.newNotification("Prova notifica " + p);
                });
            }
        }).start();

    }
}