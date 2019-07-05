package it.polimi.ingsw.view.gui.component;


import it.polimi.ingsw.model.minified.MiniPlayer;
import it.polimi.ingsw.view.gui.util.*;
import javafx.scene.layout.GridPane;

import java.util.Arrays;

/**
 * Represent the player board in the GUI
 */
public class PlayerBoardGUI extends FitObject implements Selectable {
    /**
     * image of the board
     */
    private StretchImage boardImage;
    /**
     * Overlay
     */
    private GridPane overlay;
    /**
     * The player
     */
    private MiniPlayer player;
    /**
     * Select
     */
    private SelectableComponent select;

    /**
     * Constructor of the class, load the image of the player's board and add as overlay
     * the other information for the game (damage, marks, skull track, ammo)
     * @param player The player of the board
     */
    PlayerBoardGUI(MiniPlayer player){
        this.player = player;
        String path;
        if (!player.isBoardFlipped()) {
            path = "/gui/players/" + player.getColor().toString().toLowerCase() + ".png";
        } else {
            path = "/gui/players/" + player.getColor().toString().toLowerCase() + "_flipped.png";
        }
        boardImage = new StretchImage(path);
        setContentHeight(boardImage.getImageHeight());
        setContentWidth(boardImage.getImageWidth());

        overlay = GridUtils.newGridPane(Arrays.asList(8.0, 68.0, 24.0), Arrays.asList(33.3, 33.3, 33.3));

        overlay.add(new DamageTrackPaneGUI(player.getDamageTaken()), 1, 1);
        overlay.add(new SkullTrackPaneGUI(player.getSkulls()), 1, 2);
        overlay.add(new TopPlayerPaneGUI(player.getNickname(), player.getColor() ,player.getMarks()), 1, 0);
        overlay.add(new AmmoPaneGUI(player.getAmmo()), 2, 0, 1, 3);

        if (!player.isActive()) {
            overlay.add(new ResizableImage(ImageManager.getRedMark()), 0, 0);
        }

        getChildren().addAll(boardImage, overlay);

        select = new SelectableComponent(this, player.getUuid());
    }

    /**
     * Get the height of the image
     * @return the height of the image
     */
    public double getImageHeight() {
        return boardImage.getImageHeight();
    }

    /**
     * Get the width of the image
     * @return the width of the image
     */
    public double getImageWidth() {
        return boardImage.getImageWidth();
    }

    /**
     * Get the UUID
     * @return the UUID
     */
    @Override
    public String getUuid() {
        return select.getUuid();
    }

    /**
     * Enable
     * @param notifyChange the change notifier
     */
    @Override
    public void enable(Runnable notifyChange) {
        select.enable(notifyChange);
    }

    /**
     * Set as selected
     * @param selected true as selected or false as not selected
     */
    @Override
    public void setSelected(boolean selected) {
        select.setSelected(selected);
    }

    /**
     * Disable
     */
    @Override
    public void disable() {
        select.disable();
    }
}