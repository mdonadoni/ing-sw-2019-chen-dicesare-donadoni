package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.PowerUp;
import it.polimi.ingsw.model.minified.MiniPlayer;
import javafx.geometry.Orientation;
import javafx.scene.layout.GridPane;

import java.util.List;

public class PlayerPaneGUI extends GridPane {
    private static double BOARD_PERCENT = 50;
    private static double BOARD_REDUCED_PERCENT = 65;

    private PlayerBoardGUI playerBoardGUI;
    private boolean reduced;



    public PlayerPaneGUI(MiniPlayer player, List<PowerUp> myPowerUps){
        GridUtils.setPercentColumns(this, 100);

        if (myPowerUps == null) {
            reduced = true;
            GridUtils.setPercentRows(this, 100.0 - BOARD_REDUCED_PERCENT, BOARD_REDUCED_PERCENT);
        } else {
            reduced = false;
            GridUtils.setPercentRows(this, 100.0 - BOARD_PERCENT, BOARD_PERCENT);
        }

        playerBoardGUI = new PlayerBoardGUI(player);

        add(new BottomPlayerPaneGUI(player.getWeapons(), myPowerUps), 0, 0);
        add(playerBoardGUI, 0, 1);
    }

    public PlayerPaneGUI(MiniPlayer player){
        this(player, null);
    }

    @Override
    public Orientation getContentBias() {
        return Orientation.HORIZONTAL;
    }

    @Override
    protected double computePrefHeight(double w) {
        double boardHeight = w / playerBoardGUI.getImageWidth() * playerBoardGUI.getImageHeight();
        if (reduced) {
            return boardHeight / BOARD_REDUCED_PERCENT * 100.0;
        } else {
            return boardHeight / BOARD_PERCENT * 100.0;
        }
    }
}
