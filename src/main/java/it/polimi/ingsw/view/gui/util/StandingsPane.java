package it.polimi.ingsw.view.gui.util;

import it.polimi.ingsw.common.StandingsItem;
import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.common.dialogs.Dialogs;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.text.MessageFormat;
import java.util.List;

/**
 * Represents the standings pane
 */
public class StandingsPane extends BorderPane {
    /**
     * Dialog message of the final standings
     */
    private static final String FINAL_STANDINGS = Dialogs.getDialog(Dialog.FINAL_STANDINGS);
    /**
     * The format of the standings
     */
    private static final String STANDINGS_LINE = "{0})  {1}  ({2} punti)";
    /**
     * Dimension of the font
     */
    private static final int FONT_SIZE = 25;

    /**
     * Constructor of the class
     * @param standings The list of Standings items
     */
    public StandingsPane(List<StandingsItem> standings) {
        VBox vbox = new VBox();
        BorderPane.setAlignment(vbox, Pos.CENTER);
        vbox.setAlignment(Pos.CENTER);
        vbox.setMaxSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
        setCenter(vbox);

        Font font = new Font(FONT_SIZE);
        // Text on top
        Text topText = new Text(FINAL_STANDINGS);
        topText.setFont(font);
        TextFlow topFlow = new TextFlow(topText);
        vbox.getChildren().add(topFlow);
        // Standings line
        for (StandingsItem s : standings) {
            String line = MessageFormat.format(STANDINGS_LINE, s.getPosition(), s.getNickname(), s.getPoints());
            Text text = new Text(line);
            text.setFont(font);
            TextFlow flow = new TextFlow(text);
            vbox.getChildren().add(flow);
        }
    }
}
