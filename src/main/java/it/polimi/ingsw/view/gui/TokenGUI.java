package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.PlayerToken;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;


public class TokenGUI extends GridPane {

    TokenGUI(PlayerToken token, int quantity){

        ColumnConstraints padCol = new ColumnConstraints();
        ColumnConstraints tokenCol = new ColumnConstraints();
        RowConstraints padRow = new RowConstraints();
        RowConstraints tokenRow = new RowConstraints();
        padCol.setPercentWidth(15);
        padRow.setPercentHeight(15);
        tokenCol.setPercentWidth(70);
        tokenRow.setPercentHeight(70);

        getColumnConstraints().add(padCol);
        getColumnConstraints().add(tokenCol);
        getColumnConstraints().add(padCol);
        getRowConstraints().add(padRow);
        getRowConstraints().add(tokenRow);
        getRowConstraints().add(padRow);


        String path = "/gui/players/marks/"+token.toString().toLowerCase();
        switch(quantity){
            case 2:
                path = path + "2";
                break;
            case 3:
                path = path + "3";
                break;
            default:
                break;
        }
        path = path+"token.png";

        add(new StretchImage(path), 1, 1);
    }

}
