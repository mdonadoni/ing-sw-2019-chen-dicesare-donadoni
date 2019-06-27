package it.polimi.ingsw.view.cli.util;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.PlayerToken;

public class ColorCLI {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_GREY = "\u001B[90m";

    public static final String BOLD = "\u001B[1";

    public static String turnBlack(String s){
        return ANSI_BLACK+s+ANSI_RESET;
    }

    public static String turnRed(String s){
        return ANSI_RED+s+ANSI_RESET;
    }

    public static String turnGreen(String s){
        return ANSI_GREEN+s+ANSI_RESET;
    }

    public static String turnYellow(String s){
        return ANSI_YELLOW+s+ANSI_RESET;
    }

    public static String turnBlue(String s){
        return ANSI_BLUE+s+ANSI_RESET;
    }

    public static String turnPurple(String s){
        return ANSI_PURPLE+s+ANSI_RESET;
    }

    public static String turnCyan(String s){
        return ANSI_CYAN+s+ANSI_RESET;
    }

    public static String turnGrey(String s){
        return ANSI_GREY+s+ANSI_RESET;
    }

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
