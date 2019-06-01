package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.PlayerToken;
import it.polimi.ingsw.util.ColorTranslator;
import javafx.scene.control.Cell;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

public class NicknamePaneGUI extends GridPane {

    NicknamePaneGUI(String nickname, PlayerToken color){
        ColumnConstraints nameCol = new ColumnConstraints();
        ColumnConstraints rightCol = new ColumnConstraints();
        nameCol.setPercentWidth(33);
        rightCol.setPercentWidth(64);

        RowConstraints topRow = new RowConstraints();
        RowConstraints nameRow = new RowConstraints();
        topRow.setPercentHeight(33);
        nameRow.setPercentHeight(64);

        getColumnConstraints().add(nameCol);
        getColumnConstraints().add(rightCol);
        getRowConstraints().add(topRow);
        getRowConstraints().add(nameRow);

        Label name = new Label(nickname);
        name.setMinSize(0,0);
        String readableColor = ColorTranslator.getReadableColor(color);
        this.heightProperty().addListener(event -> {
            this.setStyle("-fx-font-size: " + this.getHeight()/1.7);
        });
        name.setStyle("-fx-text-fill: " + readableColor + ";"+
                "-fx-font-weight: bold;" +
                "-fx-effect: dropshadow(gaussian, black, 4, 0.5, 0, 1);");
        add(name, 0, 1);

    }

}
