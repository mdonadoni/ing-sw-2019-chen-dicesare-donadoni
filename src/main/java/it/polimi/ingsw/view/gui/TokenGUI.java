package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.PlayerToken;
import it.polimi.ingsw.util.ColorTranslator;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;


public class TokenGUI extends GridPane {

    TokenGUI(PlayerToken token){

        ColumnConstraints padCol = new ColumnConstraints();
        ColumnConstraints tokenCol = new ColumnConstraints();
        RowConstraints padRow = new RowConstraints();
        RowConstraints tokenRow = new RowConstraints();
        padCol.setPercentWidth(20);
        padRow.setPercentHeight(20);
        tokenCol.setPercentWidth(60);
        tokenRow.setPercentHeight(60);

        getColumnConstraints().add(padCol);
        getColumnConstraints().add(tokenCol);
        getColumnConstraints().add(padCol);
        getRowConstraints().add(padRow);
        getRowConstraints().add(tokenRow);
        getRowConstraints().add(padRow);

        Pane pane = new Pane();
        pane.setStyle("-fx-background-image: url(/gui/players/marks/" + token.toString().toLowerCase() + "Token.png);" +
                        "-fx-background-size: stretch;"+
                        "-fx-effect: innershadow(two-pass-box, black, 9, 0.2, 2, -2);");
        add(pane, 1, 1);
    }

}
