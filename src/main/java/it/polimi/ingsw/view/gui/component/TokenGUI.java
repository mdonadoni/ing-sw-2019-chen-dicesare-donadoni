package it.polimi.ingsw.view.gui.component;

import it.polimi.ingsw.model.PlayerToken;
import it.polimi.ingsw.view.gui.util.GridUtils;
import it.polimi.ingsw.view.gui.util.StretchImage;
import javafx.scene.layout.GridPane;


public class TokenGUI extends GridPane {

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
