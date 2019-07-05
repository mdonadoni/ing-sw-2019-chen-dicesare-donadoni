package it.polimi.ingsw.view.gui.util;


import it.polimi.ingsw.model.PlayerToken;
import javafx.scene.paint.Color;

/**
 * Translate the color
 */
public class ColorTranslator {
    /**
     * This class should not be constructed.
     */
    private ColorTranslator() { }

    /**
     * Get the color for the CSS
     * @param token the player's color
     * @return the color
     */
    public static Color getCssColor(PlayerToken token){
        Color color = Color.BLACK;
        switch(token){
            case PURPLE:
                color=Color.MEDIUMVIOLETRED;
                break;
            case GREEN:
                color=Color.GREEN;
                break;
            case GREY:
                color=Color.DARKGRAY;
                break;
            case BLUE:
                color=Color.ROYALBLUE;
                break;
            case YELLOW:
                color=Color.GOLD;
                break;
        }
        return color;
    }

    /**
     * Get the CSS color name
     * @param token the player's color
     * @return the color name
     */
    public static String getCssColorName(PlayerToken token){
        String name= "";

        switch (token){
            case PURPLE:
                name="mediumvioletred";
                break;
            case GREEN:
                name="green";
                break;
            case GREY:
                name="darkgrey";
                break;
            case BLUE:
                name="royalblue";
                break;
            case YELLOW:
                name="gold";
                break;
        }

        return name;
    }

    /**
     * Get readable color
     * @param color the player's color
     * @return the color
     */
    public static String getReadableColor(PlayerToken color){
        String name = "";
        switch (color){
            case YELLOW:
                name = "#ffed00";
                break;
            case BLUE:
                name = "#009bb8";
                break;
            case GREY:
                name = "gainsboro";
                break;
            case GREEN:
                name = "#728f36";
                break;
            case PURPLE:
                name = "#c73186";
                break;
        }
        return name;
    }

}
