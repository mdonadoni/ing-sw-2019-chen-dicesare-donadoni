package it.polimi.ingsw.view.cli.util;

import it.polimi.ingsw.model.Cardinal;
import it.polimi.ingsw.model.LinkType;
import it.polimi.ingsw.model.minified.MiniSquare;

import java.util.List;

/**
 * This is used to get particular character for the CLI
 */
public class CharCli {
    /**
     * Vertical wall
     */
    public static final char VERTICAL_WALL='\u2551';
    /**
     * Horizontal wall
     */
    public static final char HORIZONTAL_WALL='\u2550';
    /**
     * Vertical door
     */
    public static final char VERTICAL_DOOR='\u2502';
    /**
     * Horizontal door
     */
    public static final char HORIZONTAL_DOOR='\u2500';
    /**
     * Horizontal same room
     */
    public static final char HORIZONTAL_SAME_ROOM='\u2504';
    /**
     * Vertical same room
     */
    public static final char VERTICAL_SAME_ROOM='\u2506';
    /**
     * Top left corner
     */
    public static final char TOP_LEFT_CORNER='\u2554';
    /**
     * Top right corner
     */
    public static final char TOP_RIGHT_CORNER='\u2557';
    /**
     * Bottom left corner
     */
    public static final char BOTTOM_LEFT_CORNER='\u255A';
    /**
     * Bottom right corner
     */
    public static final char BOTTOM_RIGHT_CORNER='\u255D';
    /**
     * Cross corner wall
     */
    public static final char CROSS_CORNER_WALL='\u256C';
    /**
     * T-shaped top wall corner
     */
    public static final char T_WALL_TOP_CORNER='\u2566';
    /**
     * T-shaped bottom wall corner
     */
    public static final char T_WALL_DOWN_CORNER='\u2569';
    /**
     * T-shaped left wall corner
     */
    public static final char T_WALL_LEFT='\u2563';
    /**
     * T-shaped right wall corner
     */
    public static final char T_WALL_RIGHT='\u2560';
    /**
     * Skull
     */
    public static final String SKULL = "\u2620";
    /**
     * Ammo
     */
    public static final String AMMO = "\u25FC";
    /**
     * Space
     */
    public static final char SPACE = ' ';
    /**
     * Damage token
     */
    public static final String DAMAGE_TOKEN = "\u25C6";
    /**
     * Mark token
     */
    public static final String MARK_TOKEN = "\u25CE";


    /**
     * This class should not be constructed.
     */
    private CharCli() {}

    /**
     * Concat 2 List of string, the result is the first list of string contain each
     * a string of the second list
     * @param list1 fist list
     * @param list2 second list
     */
    public static synchronized void concatRow(List<String> list1, List<String> list2){
        String s;
        for( int i = 0 ; i< list1.size() && i< list2.size(); i++)
        {
            s = list1.get(i);
            s = s.concat(list2.get(i));
            list1.remove(i);
            list1.add(i, s);
        }
    }

    /**
     * Add a certain amount of same character in a string
     * @param out The string to fill
     * @param c the character to use
     * @param n the amount of character
     * @return the string filled
     */
    public static String addChars(String out, char c, int n){
        while(n > 0){
            out = out.concat(""+c);
            n--;
        }
        return out;
    }

    /**
     * Add a certain amount space in a string
     * @param out The string to fill
     * @param space the amount of space
     * @return the string filled
     */
    public static String addSpace(String out, int space){
        return CharCli.addChars( out, CharCli.SPACE, space );
    }

    /**
     * Return the type of corner needed based on the cardinal and the squares links
     * @param ms the square
     * @param cardinal1 the first cardinal
     * @param cardinal2 the second cardinal
     * @return the needed corner
     */
    public static char getCornerChar(MiniSquare ms, Cardinal cardinal1, Cardinal cardinal2){
        char c = ' ';
        if( cardinal1 == Cardinal.NORTH || cardinal2 == Cardinal.NORTH){
            if(cardinal2 == Cardinal.WEST || cardinal1 == Cardinal.WEST){
                if(ms.getBoundary(Cardinal.WEST) == LinkType.WALL ) {
                    if( ms.getBoundary(Cardinal.NORTH) == LinkType.WALL) {
                        c = TOP_LEFT_CORNER;
                    }else{
                        c = T_WALL_RIGHT;
                    }
                }else {
                    if (ms.getBoundary(Cardinal.NORTH) == LinkType.WALL) {
                        c = T_WALL_TOP_CORNER;
                      }else{
                        c = CROSS_CORNER_WALL;
                    }
                }
            }else if(cardinal2 == Cardinal.EAST || cardinal1 == Cardinal.EAST){
                if(ms.getBoundary(Cardinal.EAST) == LinkType.WALL ) {
                    if( ms.getBoundary(Cardinal.NORTH) == LinkType.WALL) {
                        c = TOP_RIGHT_CORNER;
                    }else{
                        c = T_WALL_LEFT;
                    }
                }else {
                    if (ms.getBoundary(Cardinal.NORTH) == LinkType.WALL) {
                        c = T_WALL_TOP_CORNER;
                    }else{
                        c = CROSS_CORNER_WALL;
                    }
                }
            }
        }else if( cardinal1 == Cardinal.SOUTH || cardinal2 == Cardinal.SOUTH){
            if( cardinal2 == Cardinal.WEST || cardinal1 == Cardinal.WEST){
                if( ms.getBoundary(Cardinal.SOUTH) == LinkType.WALL){
                    if( ms.getBoundary(Cardinal.WEST) == LinkType.WALL){
                        c = BOTTOM_LEFT_CORNER;
                    }
                    else{
                        c = T_WALL_DOWN_CORNER;
                    }
                }else{
                    if( ms.getBoundary(Cardinal.WEST) == LinkType.WALL){
                        c = T_WALL_RIGHT;
                    }
                    else{
                        c = CROSS_CORNER_WALL;
                    }
                }
            }else if(cardinal2 == Cardinal.EAST || cardinal1 == Cardinal.EAST){
                if( ms.getBoundary(Cardinal.SOUTH) == LinkType.WALL){
                    if(ms.getBoundary(Cardinal.EAST) == LinkType.WALL) {
                        c = BOTTOM_RIGHT_CORNER;
                    }else{
                        c = T_WALL_DOWN_CORNER;
                    }
                }else {
                    if(ms.getBoundary(Cardinal.EAST) == LinkType.WALL) {
                        c = T_WALL_LEFT;
                    }else{
                        c = CROSS_CORNER_WALL;
                    }
                }
            }
        }
        return c;
    }

    public static char getLinkChar(MiniSquare ms, Cardinal cardinal){
        char c = ' ';
        if( cardinal == Cardinal.NORTH || cardinal == Cardinal.SOUTH){
            if( ms.getBoundary(cardinal) == LinkType.WALL){
                c = HORIZONTAL_WALL;
            }else if( ms.getBoundary(cardinal) == LinkType.DOOR){
                c = HORIZONTAL_DOOR;
            }else if( ms.getBoundary(cardinal) == LinkType.SAME_ROOM){
                c = HORIZONTAL_SAME_ROOM;
            }
        }else if(cardinal == Cardinal.WEST || cardinal == Cardinal.EAST){
            if( ms.getBoundary(cardinal) == LinkType.WALL){
                c = VERTICAL_WALL;
            }else if( ms.getBoundary(cardinal) == LinkType.DOOR){
                c = VERTICAL_DOOR;
            }else if( ms.getBoundary(cardinal) == LinkType.SAME_ROOM){
                c = VERTICAL_SAME_ROOM;
            }
        }
        return c;
    }


}
