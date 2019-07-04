package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.PlayerToken;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import java.util.List;

public class MarkTrackPaneGUI extends GridPane {

    MarkTrackPaneGUI(List<PlayerToken> marks){
        RowConstraints markRow = new RowConstraints();
        markRow.setPercentHeight(100);
        ColumnConstraints markCol = new ColumnConstraints();
        ColumnConstraints padCol = new ColumnConstraints();
        padCol.setPercentWidth(20);
        markCol.setPercentWidth(15);

        getRowConstraints().add(markRow);

        getColumnConstraints().add(padCol);
        for(int i=0; i<4; i++)
            getColumnConstraints().add(markCol);
        getColumnConstraints().add(padCol);

        int index = 1;
        for(PlayerToken color : PlayerToken.values()){
            int quantity = (int)marks.stream().filter(tk -> tk.equals(color)).count();
            if(quantity > 0){
                add(new TokenGUI(color, quantity), index, 0);
                index++;
            }
        }
    }
}
