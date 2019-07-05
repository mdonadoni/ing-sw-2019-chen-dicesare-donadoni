package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.PlayerToken;
import it.polimi.ingsw.view.gui.util.ColorTranslator;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

/**
 * Represent the nickname pane
 */
public class NicknamePaneGUI extends GridPane {
    /**
     * Small font ratio
     */
    private static final double SMALL_FONT_RATIO = 2.5;
    /**
     * Medium font ratio
     */
    private static final double MEDIUM_FONT_RATIO = 1.8;
    /**
     * Big font ratio
     */
    private static final double BIG_FONT_RATIO = 1.6;
    /**
     * Maximum length of a  nickname
     */
    private static final int MAX_MEDIUM_NICK_LENGTH = 7;
    /**
     * Minimum length of a nickname
     */
    private static final int MAX_SHORT_NICK_LENGTH = 4;

    /**
     * Constructor
     * @param nickname the nickname to show
     * @param color the color of the player
     */
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

        double ratio = SMALL_FONT_RATIO;

        if(nickname.length() <= MAX_MEDIUM_NICK_LENGTH){
            if(nickname.length() <= MAX_SHORT_NICK_LENGTH)
                ratio = BIG_FONT_RATIO;
            else
                ratio = MEDIUM_FONT_RATIO;
        }

        final double finalRatio = ratio;

        this.heightProperty().addListener(event -> this.setStyle("-fx-font-size: " + this.getHeight()/finalRatio));
        name.setStyle("-fx-text-fill: " + readableColor + ";"+
                "-fx-font-weight: bold;" +
                "-fx-effect: dropshadow(gaussian, black, 4, 0.5, 0, 1);");
        add(name, 0, 1);

    }

}
