package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.minified.MiniPlayer;
import it.polimi.ingsw.model.minified.MiniPowerUp;
import it.polimi.ingsw.view.gui.util.GridUtils;
import it.polimi.ingsw.view.gui.util.Selectable;
import it.polimi.ingsw.view.gui.util.SelectableContainer;
import javafx.geometry.Orientation;
import javafx.scene.layout.GridPane;

import java.util.List;

/**
 * Represent the player pane
 */
public class PlayerPaneGUI extends GridPane implements SelectableContainer {
    /**
     * The percent of board
     */
    private static final double BOARD_PERCENT = 50;
    /**
     * The percent of reduced board
     */
    private static final double BOARD_REDUCED_PERCENT = 65;
    /**
     * The player board GUI
     */
    private PlayerBoardGUI playerBoard;
    /**
     * The player' card pane
     */
    private PlayerCardsPaneGUI playerCards;
    /**
     * Define if it's reduced
     */
    private boolean reduced;


    /**
     * Constructor, generate the player pane and the cards pane
     * @param player
     * @param myPowerUps
     */
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

    /**
     * The player with no power-ups
     * @param player
     */
    public PlayerPaneGUI(MiniPlayer player){
        this(player, null);
    }

    /**
     * Get content bias
     * @return Orientation
     */
    @Override
    public Orientation getContentBias() {
        return Orientation.HORIZONTAL;
    }

    /**
     * Compute height
     * @param w to calculate the height
     * @return The height
     */
    @Override
    protected double computePrefHeight(double w) {
        double boardHeight = w / playerBoard.getImageWidth() * playerBoard.getImageHeight();
        if (reduced) {
            return boardHeight / BOARD_REDUCED_PERCENT * 100.0;
        } else {
            return boardHeight / BOARD_PERCENT * 100.0;
        }
    }

    /**
     * Find selectable
     * @param uuid UUID of the selectable
     * @return the selectable object
     */
    @Override
    public Selectable findSelectable(String uuid) {
        if (playerBoard.getUuid().equals(uuid)) {
            return playerBoard;
        }
        return playerCards.findSelectable(uuid);
    }
}
