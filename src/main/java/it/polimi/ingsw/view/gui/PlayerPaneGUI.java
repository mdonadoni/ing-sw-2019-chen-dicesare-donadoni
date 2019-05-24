package it.polimi.ingsw.view.gui;


import it.polimi.ingsw.model.minified.MiniPlayer;
import it.polimi.ingsw.util.ResourceException;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;


public class PlayerPaneGUI extends GridPane {

    PlayerPaneGUI(MiniPlayer player){
        String path = "/gui/players/" + player.getColor().toString().toLowerCase() + ".png";
        if (getClass().getResource(path) == null) {
            throw new ResourceException("Cannot find player pane gui");
        }
        setStyle("-fx-background-image: url(" + path + ");" +
                "-fx-background-size: stretch;");

        RowConstraints row = new RowConstraints();
        row.setPercentHeight(33.3);
        for(int i=0; i<3; i++)
            getRowConstraints().add(row);

        ColumnConstraints leftPad = new ColumnConstraints();
        ColumnConstraints damages = new ColumnConstraints();
        ColumnConstraints rightPad = new ColumnConstraints();
        leftPad.setPercentWidth(8);
        rightPad.setPercentWidth(24);
        damages.setPercentWidth(68);
        getColumnConstraints().add(leftPad);
        getColumnConstraints().add(damages);
        getColumnConstraints().add(rightPad);

        add(new DamageTrackPaneGUI(player.getDamageTaken()), 1, 1);
        add(new SkullTrackPaneGUI(player.getSkulls()), 1, 2);
        add(new TopPlayerPaneGUI(player.getNickname(), player.getColor() ,player.getMarks()), 1, 0);
        add(new AmmoPaneGUI(player.getAmmo()), 2, 0, 1, 3);
    }

}