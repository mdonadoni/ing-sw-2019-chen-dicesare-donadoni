package it.polimi.ingsw.view.gui;


import it.polimi.ingsw.model.PlayerToken;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.List;

public class DamageTrackPaneGUI extends GridPane {

    DamageTrackPaneGUI(List<PlayerToken> damage){
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(8);
        ColumnConstraints pad = new ColumnConstraints();
        pad.setPercentWidth(1);
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(100);
        getRowConstraints().add(row);

        // The following shit is because the player pane asset is braindead and it has no symmetry.
        // notice that there are some pipes between damage squares, these pipes cause the symmetry problem

        getColumnConstraints().add(pad);
        for(int i=0; i<2; i++)
            getColumnConstraints().add(col);
        getColumnConstraints().add(pad);
        for(int i=0; i<3; i++)
            getColumnConstraints().add(col);
        getColumnConstraints().add(pad);
        for(int i=0; i<5; i++)
            getColumnConstraints().add(col);
        getColumnConstraints().add(pad);
        for(int i=0; i<2; i++)
            getColumnConstraints().add(col);
        for(int i=0; i<damage.size(); i++){
            int j = i+1;
            if(i>=2)
                j = i+2;
            if(i>=5)
                j = i+3;
            if(i>=10)
                j = i+4;
            add(new TokenGUI(damage.get(i), 1), j, 0);
        }
    }
}
