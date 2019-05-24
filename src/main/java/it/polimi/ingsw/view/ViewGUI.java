package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.minified.MiniGameBoard;
import it.polimi.ingsw.model.minified.MiniMatch;
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

        Player playerA = new Player("thatDc", PlayerToken.BLUE);
        playerA.addDamage(PlayerToken.PURPLE, 2);
        playerA.addDamage(PlayerToken.BLUE, 2);
        playerA.addDamage(PlayerToken.GREEN, 3);
        playerA.addDamage(PlayerToken.YELLOW, 1);
        playerA.setSkulls(3);
        playerA.addMark(PlayerToken.YELLOW, 3);
        playerA.addMark(PlayerToken.BLUE, 3);
        playerA.addMark(PlayerToken.PURPLE, 3);
        playerA.addMark(PlayerToken.GREEN, 3);
        playerA.addAmmo(AmmoColor.BLUE);
        playerA.addAmmo(AmmoColor.RED);
        playerA.addAmmo(AmmoColor.YELLOW);
        playerA.addAmmo(AmmoColor.YELLOW);
        playerA.addAmmo(AmmoColor.RED);
        playerA.grabWeapon(new Weapon("cyberblade"));
        playerA.grabWeapon(new Weapon("rocketlauncher"));
        playerA.grabWeapon(new Weapon("sledgehammer"));
        MiniPlayer miniPlayerA = new MiniPlayer(playerA);

        Player playerB = new Player("Mark03", PlayerToken.YELLOW);
        playerB.addDamage(PlayerToken.GREY, 2);
        playerB.addMark(PlayerToken.BLUE, 1);
        playerB.setSkulls(1);
        playerB.grabWeapon(new Weapon("lockrifle"));

        Player playerC = new Player("Simone", PlayerToken.GREEN);
        playerC.addDamage(PlayerToken.YELLOW, 2);
        playerC.addDamage(PlayerToken.BLUE, 3);
        playerC.addMark(PlayerToken.PURPLE, 1);
        playerC.addMark(PlayerToken.GREY, 2);
        playerC.setSkulls(0);
        playerC.addAmmo(AmmoColor.YELLOW);
        playerC.addAmmo(AmmoColor.YELLOW);
        playerC.addAmmo(AmmoColor.RED);

        Player playerD = new Player("Riky", PlayerToken.PURPLE);
        playerD.addDamage(PlayerToken.GREY, 3);
        playerD.addMark(PlayerToken.GREEN, 2);
        playerD.setSkulls(2);
        playerD.grabWeapon(new Weapon("tractorbeam"));
        playerD.grabWeapon(new Weapon("hellion"));

        Player playerE = new Player("Fuljo", PlayerToken.GREY);
        playerE.addDamage(PlayerToken.YELLOW, 2);
        playerE.addMark(PlayerToken.BLUE, 1);
        playerE.setSkulls(0);
        playerE.addAmmo(AmmoColor.BLUE);
        playerE.addAmmo(AmmoColor.RED);

        MiniPlayer miniPlayerB = new MiniPlayer(playerB);
        MiniPlayer miniPlayerC = new MiniPlayer(playerC);
        MiniPlayer miniPlayerD = new MiniPlayer(playerD);
        MiniPlayer miniPlayerE = new MiniPlayer(playerE);

        List<MiniPlayer> others = new ArrayList<>();
        others.add(miniPlayerB);
        others.add(miniPlayerC);
        others.add(miniPlayerD);
        others.add(miniPlayerE);

        primaryStage.setScene(new Scene(new UserViewGUI(miniPlayerA, others, board), 600, 400));
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