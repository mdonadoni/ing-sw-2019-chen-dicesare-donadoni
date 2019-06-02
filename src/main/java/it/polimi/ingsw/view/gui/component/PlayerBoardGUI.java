package it.polimi.ingsw.view.gui.component;


import it.polimi.ingsw.model.minified.MiniPlayer;
import it.polimi.ingsw.view.gui.util.Selectable;
import it.polimi.ingsw.view.gui.util.FitObject;
import it.polimi.ingsw.view.gui.util.GridUtils;
import it.polimi.ingsw.view.gui.util.SelectableComponent;
import it.polimi.ingsw.view.gui.util.StretchImage;
import javafx.scene.layout.GridPane;

import java.util.Arrays;


public class PlayerBoardGUI extends FitObject implements Selectable {
    StretchImage boardImage;
    private GridPane overlay;
    MiniPlayer player;
    SelectableComponent select;
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

        select = new SelectableComponent(this, player.getUuid());
    }

    public double getImageHeight() {
        return boardImage.getImageHeight();
    }

    public double getImageWidth() {
        return boardImage.getImageWidth();
    }

    @Override
    public String getUuid() {
        return select.getUuid();
    }

    @Override
    public void enable(Runnable notifyChange) {
        select.enable(notifyChange);
    }

    @Override
    public void setSelected(boolean selected) {
        select.setSelected(selected);
    }

    @Override
    public void disable() {
        select.disable();
    }
}