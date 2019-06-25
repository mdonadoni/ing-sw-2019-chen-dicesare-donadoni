package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.PowerUp;
import it.polimi.ingsw.model.minified.MiniGameBoard;
import it.polimi.ingsw.model.minified.MiniModel;
import it.polimi.ingsw.model.minified.MiniPlayer;
import it.polimi.ingsw.util.ResourceManager;
import it.polimi.ingsw.view.gui.util.GridUtils;
import it.polimi.ingsw.view.gui.util.Selectable;
import it.polimi.ingsw.view.gui.util.SelectableContainer;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.util.ArrayList;
import java.util.List;


public class UserViewGUI extends GridPane implements SelectableContainer {

    private static final double BOARDSPACE = 65;

    private OtherPlayersPaneGUI otherPlayers;
    private GameBoardGUI gameBoard;
    private PlayerPaneGUI player;
    private ActionsPaneGUI actions;

    public UserViewGUI(MiniModel miniModel){
        GridUtils.setPercentColumns(this, BOARDSPACE, 100.0 - BOARDSPACE);
        GridUtils.setPercentRows(this, 66.6, 33.3);

        MiniGameBoard miniGb = miniModel.getMatch().getGameBoard();
        MiniPlayer myMini = miniModel.getMyMiniPlayer();
        List<MiniPlayer> others = miniModel.getMatch().getPlayers();
        others.remove(myMini);

        List<PowerUp> visiblePowerUps = new ArrayList<>();
        visiblePowerUps.addAll(miniModel.getMyPowerUps());
        visiblePowerUps.addAll(miniModel.getMyDrawnPowerUps());

        otherPlayers = new OtherPlayersPaneGUI(others);
        gameBoard = new GameBoardGUI(miniGb);
        player = new PlayerPaneGUI(myMini, visiblePowerUps);
        actions = new ActionsPaneGUI(miniModel.getMatch().getCurrentTurn().getAvaibleActions());

        ScrollPane scroll = new ScrollPane(otherPlayers);
        scroll.setFitToWidth(true);
        scroll.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        scroll.getStylesheets().add("/gui/css/stylesheet.css");

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gameBoard);
        borderPane.setLeft(actions);

        add(borderPane, 0, 0);
        add(player, 0, 1);
        add(scroll, 1, 0, 1, 2);

        setBackground(new Background(
                new BackgroundFill(
                new ImagePattern(
                        new Image(ResourceManager.get("/gui/background.jpg"))
                ), CornerRadii.EMPTY, Insets.EMPTY
        )));
    }

    @Override
    public Selectable findSelectable(String uuid) {
        Selectable res = gameBoard.findSelectable(uuid);
        if (res == null) res = actions.findSelectable(uuid);
        if (res == null) res = player.findSelectable(uuid);
        if (res == null) res = otherPlayers.findSelectable(uuid);
        return res;
    }
}
