package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.common.StandingsItem;
import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.model.minified.MiniModel;
import it.polimi.ingsw.view.gui.component.UserViewGUI;
import it.polimi.ingsw.view.gui.util.Notification;
import it.polimi.ingsw.view.gui.util.Selectable;
import it.polimi.ingsw.view.gui.util.StandingsPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ViewGUI extends Application {
    Stage primaryStage;
    DummyViewGUI dummy;
    UserViewGUI mainPane;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Create mainPane for game view
        mainPane = new UserViewGUI();

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

        Match match = new Match(nicks, new JsonModelFactory(BoardType.SMALL));

        match.getGameBoard().addKill(Arrays.asList(PlayerToken.YELLOW, PlayerToken.GREY));
        match.getGameBoard().addKill(Arrays.asList(PlayerToken.YELLOW, PlayerToken.GREY, PlayerToken.GREEN));
        match.getGameBoard().addKill(Arrays.asList(PlayerToken.BLUE));


        Player tempPlayer = match.getPlayerByNickname("thatDc");
        tempPlayer.addDamageWithoutMarks(PlayerToken.PURPLE, 2);
        tempPlayer.addDamageWithoutMarks(PlayerToken.BLUE, 2);
        tempPlayer.addDamageWithoutMarks(PlayerToken.GREEN, 3);
        tempPlayer.addDamageWithoutMarks(PlayerToken.YELLOW, 1);
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
        tempPlayer.addDamageWithoutMarks(PlayerToken.GREY, 2);
        tempPlayer.addMark(PlayerToken.BLUE, 1);
        tempPlayer.setSkulls(1);
        tempPlayer.grabWeapon(new Weapon("lockrifle"));

        tempPlayer = match.getPlayerByNickname("Simone");
        tempPlayer.addDamageWithoutMarks(PlayerToken.YELLOW, 2);
        tempPlayer.addDamageWithoutMarks(PlayerToken.BLUE, 3);
        tempPlayer.addMark(PlayerToken.PURPLE, 1);
        tempPlayer.addMark(PlayerToken.GREY, 2);
        tempPlayer.setSkulls(0);
        tempPlayer.addAmmo(AmmoColor.YELLOW);
        tempPlayer.addAmmo(AmmoColor.YELLOW);
        tempPlayer.addAmmo(AmmoColor.RED);

        tempPlayer = match.getPlayerByNickname("Fuljo");
        tempPlayer.addDamageWithoutMarks(PlayerToken.GREY, 3);
        tempPlayer.addMark(PlayerToken.GREEN, 2);
        tempPlayer.setSkulls(2);
        tempPlayer.grabWeapon(new Weapon("tractorbeam"));
        tempPlayer.grabWeapon(new Weapon("hellion"));

        tempPlayer = match.getPlayerByNickname("Ricky");
        tempPlayer.addDamageWithoutMarks(PlayerToken.YELLOW, 2);
        tempPlayer.addMark(PlayerToken.BLUE, 1);
        tempPlayer.setSkulls(0);
        tempPlayer.addAmmo(AmmoColor.BLUE);
        tempPlayer.addAmmo(AmmoColor.RED);

        MiniModel miniModel = new MiniModel(match, match.getPlayerByNickname("thatDc"));

        userView = new ModelGUI(miniModel);
        confirmSelection = new Button("Conferma");
        confirmSelection.setDisable(true);

        BorderPane main = new BorderPane();
        main.setCenter(userView);
        main.setBottom(confirmSelection);

        Weapon w = new Weapon(WeaponType.ZX2);
        MiniWeapon mw = new MiniWeapon(w);
        WeaponGUI wgui = null;
        try {
            wgui = new WeaponGUI(mw, false);
            for (MiniAttack atk : mw.getAttacks()) {
                wgui.findSelectable(atk.getUuid()).enable(() -> {});
                if (atk.hasBonusMovement()) {
                    wgui.findSelectable(atk.getBonusMovement().getUuid()).enable(() -> {});
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }


        primaryStage.setScene(new Scene(wgui, 600, 400));

        primaryStage.show();
        primaryStage.setMinWidth(854);
        primaryStage.setMinHeight(480);*/
    }

    @Override
    public void stop() {
        dummy.closeConnection();
        // fix to close application
        System.exit(0);
    }

    public List<String> selectObject(List<String> objUuid, int min, int max, Dialog dialog) {
        // find selectables
        List<Selectable> selectables = new ArrayList<>();
        for (String uuid : objUuid) {
            Selectable s = mainPane.findSelectable(uuid);
            if (s == null) {
                throw new RuntimeException("Selectable not found: " + uuid);
            }
            selectables.add(s);
        }

        // Start SelectionManager
        SelectionManager selectionManager = new SelectionManager(selectables, mainPane.getConfirmButton(), min, max);
        selectionManager.start();
        // Put dialog text
        mainPane.setDialog(dialog, Integer.toString(min), Integer.toString(max));
        // Get selected
        List<String> selected = selectionManager.getSelected();
        // Remove dialog text
        mainPane.removeDialog();
        return selected;
    }

    public void showMessage(String message) {
        Platform.runLater(() -> Notification.newNotification(message));
    }

    public void updateModel(MiniModel model) {
        mainPane.setModel(model);
        Platform.runLater(() -> primaryStage.getScene().setRoot(mainPane));
    }

    public void notifyEndMatch(List<StandingsItem> standings) {
        Platform.runLater(() -> primaryStage.getScene().setRoot(new StandingsPane(standings)));
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