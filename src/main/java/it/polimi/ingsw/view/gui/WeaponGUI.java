package it.polimi.ingsw.view.gui;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;

public class WeaponGUI extends GridPane {
    private static final double ROWPAD = 5;
    private static final double COLPAD = 10;
    public WeaponGUI(String name){
        ColumnConstraints padCol = new ColumnConstraints();
        ColumnConstraints weaponCol = new ColumnConstraints();
        RowConstraints padRow = new RowConstraints();
        RowConstraints weaponRow = new RowConstraints();

        padCol.setPercentWidth(COLPAD);
        padRow.setPercentHeight(ROWPAD);
        weaponCol.setPercentWidth(100-(COLPAD*2));
        weaponRow.setPercentHeight(100-(ROWPAD*2));

        getColumnConstraints().add(padCol);
        getColumnConstraints().add(weaponCol);
        getColumnConstraints().add(padCol);
        getRowConstraints().add(padRow);
        getRowConstraints().add(weaponRow);
        getRowConstraints().add(padRow);

        Pane weapon = new Pane();
        weapon.setStyle("-fx-background-image: url(/gui/weapons/"+name.toLowerCase()+".png);"+
                        "-fx-background-size: stretch;");
        add(weapon, 1, 1);
    }
}
