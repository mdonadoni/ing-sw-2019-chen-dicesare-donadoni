package it.polimi.ingsw.view.gui;


import it.polimi.ingsw.model.minified.MiniPlayer;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;

import java.util.Arrays;


public class PlayerBoardGUI extends FitObject {
    StretchImage boardImage;
    private GridPane overlay;
    MiniPlayer player;
    PlayerBoardGUI(MiniPlayer player){
        this.player = player;
        String path = "/gui/players/" + player.getColor().toString().toLowerCase() + ".png";
        boardImage = new StretchImage(path);
        setContentHeight(boardImage.getImageHeight());
        setContentWidth(boardImage.getImageWidth());

        overlay = GridUtils.newGridPane(Arrays.asList(8.0, 68.0, 24.0), Arrays.asList(33.3, 33.3, 33.3));

        overlay.add(new DamageTrackPaneGUI(player.getDamageTaken()), 1, 1);
        overlay.add(new SkullTrackPaneGUI(player.getSkulls()), 1, 2);
        overlay.add(new TopPlayerPaneGUI(player.getNickname(), player.getColor() ,player.getMarks()), 1, 0);
        overlay.add(new AmmoPaneGUI(player.getAmmo()), 2, 0, 1, 3);

        getChildren().addAll(boardImage, overlay);

        setEffect(new DropShadow());
    }

    public double getImageHeight() {
        return boardImage.getImageHeight();
    }

    public double getImageWidth() {
        return boardImage.getImageWidth();
    }
}