package it.polimi.ingsw.view.cli.util;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.PlayerToken;

/**
 * This class is used to get the color to use in the CLI
 */
public class ColorCLI {
    /**
     * Reset to the standard color
     */
    public static final String ANSI_RESET = "\u001B[0m";
    /**
     * Black
     */
    public static final String ANSI_BLACK = "\u001B[30m";
    /**
     * Red
     */
    public static final String ANSI_RED = "\u001B[31m";
    /**
     * Green
     */
    public static final String ANSI_GREEN = "\u001B[32m";
    /**
     * Yellow
     */
    public static final String ANSI_YELLOW = "\u001B[33m";
    /**
     * Blue
     */
    public static final String ANSI_BLUE = "\u001B[34m";
    /**
     * Purple
     */
    public static final String ANSI_PURPLE = "\u001B[35m";
    /**
     * Cyan
     */
    public static final String ANSI_CYAN = "\u001B[36m";
    /**
     * Grey
     */
    public static final String ANSI_GREY = "\u001B[90m";
    /**
     * Turn the character font to bold
     */
    public static final String BOLD = "\u001B[1";
    /**
     * Line the character
     */
    public static final String ANSI_STRIKETHROUGH = "\u001B[9m";
    /**
     * Clear the screen character
     */
    public static final String ANSI_CLS = "\u001B[2J";


    /**
     * This class should not be constructed.
     */
    private ColorCLI() {}

    /**
     * Turn the character black
     * @param s the character to turn black
     * @return the black character
     */
    public static String turnBlack(String s){
        return ANSI_BLACK+s+ANSI_RESET;
    }
    /**
     * Turn the character red
     * @param s the character to turn red
     * @return the red character
     */
    public static String turnRed(String s){
        return ANSI_RED+s+ANSI_RESET;
    }
    /**
     * Turn the character green
     * @param s the character to turn green
     * @return the green character
     */
    public static String turnGreen(String s){
        return ANSI_GREEN+s+ANSI_RESET;
    }
    /**
     * Turn the character yellow
     * @param s the character to turn yellow
     * @return the yellow character
     */
    public static String turnYellow(String s){
        return ANSI_YELLOW+s+ANSI_RESET;
    }
    /**
     * Turn the character blue
     * @param s the character to turn blue
     * @return the blue character
     */
    public static String turnBlue(String s){
        return ANSI_BLUE+s+ANSI_RESET;
    }
    /**
     * Turn the character purple
     * @param s the character to turn purple
     * @return the purple character
     */
    public static String turnPurple(String s){
        return ANSI_PURPLE+s+ANSI_RESET;
    }
    /**
     * Turn the character cyan
     * @param s the character to turn cyan
     * @return the cyan character
     */
    public static String turnCyan(String s){
        return ANSI_CYAN+s+ANSI_RESET;
    }
    /**
     * Turn the character grey
     * @param s the character to turn grey
     * @return the grey character
     */
    public static String turnGrey(String s){
        return ANSI_GREY+s+ANSI_RESET;
    }

    /**
     * Turn the character strike through
     * @param s the character to turn strike through
     * @return the strike through character
     */
    public static String turnStrikethrough(String s){
        return ANSI_STRIKETHROUGH+s+ANSI_RESET;
    }

    /**
     * Turn the string to the players color
     * @param pt player's color
     * @param s The string to change color
     * @return the colored string
     */
    public static String getPlayerColor(PlayerToken pt, String s){
        String out;
        if (pt==PlayerToken.BLUE){
            out = ColorCLI.turnBlue(s);
        }
        else if (pt==PlayerToken.GREEN){
            out = ColorCLI.turnGreen(s);
        }
        else if (pt==PlayerToken.GREY){
            out = ColorCLI.turnGrey(s);
        }
        else if (pt==PlayerToken.PURPLE){
            out = ColorCLI.turnPurple(s);
        }
        else if (pt==PlayerToken.YELLOW){
            out = ColorCLI.turnYellow(s);
        }
        else
            out = null;
        return out;
    }
    /**
     * Turn the string to the ammo color
     * @param ac ammo's color
     * @param s The string to change color
     * @return the colored string
     */
    public static String getAmmoColor(AmmoColor ac, String s){
        String out;
        if (ac==AmmoColor.BLUE){
            out = ColorCLI.turnBlue(s);
        }
        else if (ac==AmmoColor.RED){
            out = ColorCLI.turnRed(s);
        }
        else if (ac==AmmoColor.YELLOW){
            out = ColorCLI.turnYellow(s);
        }
        else
            out = null;
        return out;
    }

}
