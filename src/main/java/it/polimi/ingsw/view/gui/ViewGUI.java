package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.minified.MiniModel;
import it.polimi.ingsw.view.gui.component.UserViewGUI;
import it.polimi.ingsw.view.gui.util.Notification;
import it.polimi.ingsw.view.gui.util.Selectable;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ViewGUI extends Application {
    Stage primaryStage;
    DummyViewGUI dummy;
    UserViewGUI userView;
    BorderPane mainPane;
    Button confirmSelection;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        mainPane = new BorderPane();
        confirmSelection = new Button("Conferma");
        confirmSelection.setDisable(true);
        mainPane.setBottom(confirmSelection);

        // Construct dummy view
        dummy = new DummyViewGUI(this);

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

        Match match = new Match(nicks, new JsonModelFactory(BoardType.BIG));
        match.getGameBoard().refillAmmoTile();
        match.getGameBoard().getBoard().getSpawnPointByColor(AmmoColor.BLUE).addWeapon(new Weapon("thor"));
        match.getGameBoard().getBoard().getSpawnPointByColor(AmmoColor.BLUE).addWeapon(new Weapon("zx-2"));
        match.getGameBoard().getBoard().getSpawnPointByColor(AmmoColor.BLUE).addWeapon(new Weapon("powerglove"));


        match.getGameBoard().getBoard().getSpawnPointByColor(AmmoColor.RED).addWeapon(new Weapon("heatseeker"));
        match.getGameBoard().getBoard().getSpawnPointByColor(AmmoColor.RED).addWeapon(new Weapon("railgun"));
        match.getGameBoard().getBoard().getSpawnPointByColor(AmmoColor.RED).addWeapon(new Weapon("plasmagun"));


        match.getGameBoard().getBoard().getSpawnPointByColor(AmmoColor.YELLOW).addWeapon(new Weapon("electroscythe"));
        match.getGameBoard().getBoard().getSpawnPointByColor(AmmoColor.YELLOW).addWeapon(new Weapon("grenadelauncher"));
        match.getGameBoard().getBoard().getSpawnPointByColor(AmmoColor.YELLOW).addWeapon(new Weapon("furnace"));


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
        PowerUp p1, p2;
        p1 = new PowerUp(PowerUpType.NEWTON, AmmoColor.YELLOW);
        p2 = new PowerUp(PowerUpType.NEWTON, AmmoColor.BLUE);
        tempPlayer.addPowerUp(p1);
        tempPlayer.addPowerUp(p2);
        tempPlayer.addPowerUp(new PowerUp(PowerUpType.NEWTON, AmmoColor.RED));
        tempPlayer.addDrawnPowerUp(new PowerUp(PowerUpType.TELEPORTER, AmmoColor.YELLOW));


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

        userView = new UserViewGUI(miniModel);
        confirmSelection = new Button("Conferma");
        confirmSelection.setDisable(true);

        BorderPane main = new BorderPane();
        main.setCenter(userView);
        main.setBottom(confirmSelection);

        primaryStage.setScene(new Scene(main, 600, 400));

        primaryStage.show();
        primaryStage.setMinWidth(854);
        primaryStage.setMinHeight(480);

        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(selectObject(
                    Arrays.asList(miniModel.getMatch().getCurrentTurn().getAvaibleActions().get(0).getUuid(),
                            miniModel.getMatch().getCurrentTurn().getAvaibleActions().get(1).getUuid()),
                    1, 1));
        }).start();*/
    }

    public void stop() {
        dummy.closeConnection();
    }

    public List<String> selectObject(List<String> objUuid, int min, int max) {
        List<Selectable> selectables = new ArrayList<>();
        for (String uuid : objUuid) {
            Selectable s = userView.findSelectable(uuid);
            if (s == null) {
                throw new RuntimeException("Selectable not found: " + uuid);
            }
            selectables.add(s);
        }

        SelectionManager selectionManager = new SelectionManager(selectables, confirmSelection, min, max);
        selectionManager.start();
        return selectionManager.getSelected();
    }

    public void showMessage(String message) {
        Platform.runLater(() -> Notification.newNotification(message));
    }

    public void updateModel(MiniModel model) {
        Platform.runLater(() -> {
            userView = new UserViewGUI(model);
            mainPane.setCenter(userView);
            primaryStage.getScene().setRoot(mainPane);
        });
    }

    public void disconnect() {
        Platform.runLater(() -> {
            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
            dialog.initOwner(primaryStage);
            dialog.setTitle("Connessione con il server chiusa");
            dialog.setHeaderText("La connessione con il server è stata chiusa.");
            dialog.setContentText("Vuoi chiudere l'applicazione?");
            Optional<ButtonType> res = dialog.showAndWait();
            if (res.get() == ButtonType.OK) {
                Platform.exit();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}