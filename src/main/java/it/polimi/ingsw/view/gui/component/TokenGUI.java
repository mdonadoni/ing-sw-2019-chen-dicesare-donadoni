package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.PlayerToken;
import it.polimi.ingsw.view.gui.util.GridUtils;
import it.polimi.ingsw.view.gui.util.StretchImage;
import javafx.scene.layout.GridPane;

/**
 * Represent the token of the player in the GUI
 */
public class TokenGUI extends GridPane {
    /**
     * Constructor, load the token from file
     * @param token Player's token to load
     * @param quantity quantity of token
     */
    TokenGUI(PlayerToken token, int quantity){
        GridUtils.setPercentColumns(this, 10, 80, 10);
        GridUtils.setPercentRows(this, 10, 80, 10);

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
