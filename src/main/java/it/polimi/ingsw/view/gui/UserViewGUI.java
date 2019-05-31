package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.PowerUp;
import it.polimi.ingsw.model.minified.MiniGameBoard;
import it.polimi.ingsw.model.minified.MiniModel;
import it.polimi.ingsw.model.minified.MiniPlayer;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;

import java.util.ArrayList;
import java.util.List;


public class UserViewGUI extends GridPane {

    private static final double BOARDSPACE = 65;

    public UserViewGUI(MiniModel miniModel){
        GridUtils.setPercentColumns(this, BOARDSPACE, 100.0 - BOARDSPACE);
        GridUtils.setPercentRows(this, 66.6, 33.3);

        MiniGameBoard miniGb = miniModel.getMatch().getGameBoard();
        MiniPlayer myMini = miniModel.getMyMiniPlayer();
        List<MiniPlayer> otherPlayers = miniModel.getMatch().getPlayers();
        otherPlayers.remove(myMini);

        List<PowerUp> visiblePowerUps = new ArrayList<>();
        visiblePowerUps.addAll(miniModel.getMyPowerUps());
        visiblePowerUps.addAll(miniModel.getMyDrawnPowerUps());

        getStylesheets().add("/gui/css/stylesheet.css");

        ScrollPane scroll = new ScrollPane(new OtherPlayersPaneGUI(otherPlayers));
        scroll.setFitToWidth(true);
        //scroll.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        //scroll.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        scroll.setStyle("-fx-background-color: transparent;");

        add(new GameBoardGUI(miniGb), 0, 0);
        add(new PlayerPaneGUI(myMini, visiblePowerUps), 0, 1);
        add(scroll, 1, 0, 1, 2);

        setBackground(new Background(
                new BackgroundFill(
                new ImagePattern(
                        new Image(getClass().getResourceAsStream("/gui/background.jpg"))
                ), CornerRadii.EMPTY, Insets.EMPTY
        )));
    }
}
