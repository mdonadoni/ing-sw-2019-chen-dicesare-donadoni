package it.polimi.ingsw.view;

import it.polimi.ingsw.model.minified.MiniModel;
import it.polimi.ingsw.view.gui.LoginPane;
import it.polimi.ingsw.view.gui.Notification;
import it.polimi.ingsw.view.gui.UserViewGUI;
import it.polimi.ingsw.view.gui.WaitingPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class ViewGUI extends Application {
    Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Construct dummy view
        DummyViewGUI dummy = new DummyViewGUI(this);

        primaryStage.setTitle("Adrenalina AM26");

        // Initialize login panel
        LoginPane login = new LoginPane();
        login.setLoginCallback(dummy::loginCallback);
        login.connectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                primaryStage.getScene().setRoot(new WaitingPane());
            }
        });

        primaryStage.setScene(new Scene(login, 854, 480));
        primaryStage.setMinWidth(854);
        primaryStage.setMinHeight(480);
        primaryStage.show();

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
        primaryStage.setMinHeight(480);*/

    }

    public List<String> selectObject(List<String> objUuid, int min, int max) {
        // TODO
        return null;
    }

    public void showMessage(String message) {
        Platform.runLater(() -> Notification.newNotification(message));
    }

    public void updateModel(MiniModel model) {
        Platform.runLater(() ->
                primaryStage.getScene().setRoot(new UserViewGUI(model))
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}