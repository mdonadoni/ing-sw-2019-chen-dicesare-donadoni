package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.minified.MiniPlayer;
import it.polimi.ingsw.model.minified.MiniPowerUp;
import it.polimi.ingsw.view.gui.util.GridUtils;
import it.polimi.ingsw.view.gui.util.Selectable;
import it.polimi.ingsw.view.gui.util.SelectableContainer;
import javafx.geometry.Orientation;
import javafx.scene.layout.GridPane;

import java.util.List;

public class PlayerPaneGUI extends GridPane implements SelectableContainer {
    private static final double BOARD_PERCENT = 50;
    private static final double BOARD_REDUCED_PERCENT = 65;

    private PlayerBoardGUI playerBoard;
    private PlayerCardsPaneGUI playerCards;
    private boolean reduced;



    public PlayerPaneGUI(MiniPlayer player, List<MiniPowerUp> myPowerUps){
        GridUtils.setPercentColumns(this, 100);

        if (myPowerUps == null) {
            reduced = true;
            GridUtils.setPercentRows(this, 100.0 - BOARD_REDUCED_PERCENT, BOARD_REDUCED_PERCENT);
        } else {
            reduced = false;
            GridUtils.setPercentRows(this, 100.0 - BOARD_PERCENT, BOARD_PERCENT);
        }

        playerBoard = new PlayerBoardGUI(player);
        playerCards = new PlayerCardsPaneGUI(player.getWeapons(), myPowerUps);

        add(playerCards, 0, 0);
        add(playerBoard, 0, 1);
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
        double boardHeight = w / playerBoard.getImageWidth() * playerBoard.getImageHeight();
        if (reduced) {
            return boardHeight / BOARD_REDUCED_PERCENT * 100.0;
        } else {
            return boardHeight / BOARD_PERCENT * 100.0;
        }
    }

    @Override
    public Selectable findSelectable(String uuid) {
        if (playerBoard.getUuid().equals(uuid)) {
            return playerBoard;
        }
        return playerCards.findSelectable(uuid);
    }
}
