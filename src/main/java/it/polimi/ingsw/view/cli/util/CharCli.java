package it.polimi.ingsw.view.cli.util;

import it.polimi.ingsw.model.Cardinal;
import it.polimi.ingsw.model.LinkType;
import it.polimi.ingsw.model.minified.MiniSquare;

import java.util.List;

public class CharCli {
    public static final char VERTICAL_WALL='\u2551';
    public static final char HORIZONTAL_WALL='\u2550';
    public static final char VERTICAL_DOOR='\u2502';
    public static final char HORIZONTAL_DOOR='\u2500';
    public static final char HORIZONTAL_SAME_ROOM='\u2504';
    public static final char VERTICAL_SAME_ROOM='\u2506';
    public static final char TOP_LEFT_CORNER='\u2554';
    public static final char TOP_RIGHT_CORNER='\u2557';
    public static final char BOTTOM_LEFT_CORNER='\u255A';
    public static final char BOTTOM_RIGHT_CORNER='\u255D';
    public static final char CROSS_CORNER_WALL='\u256C';
    public static final char T_WALL_TOP_CORNER='\u2566';
    public static final char T_WALL_DOWN_CORNER='\u2569';
    public static final char T_WALL_LEFT='\u2563';
    public static final char T_WALL_RIGHT='\u2560';
    public static final String SKULL = "\u2620";
    public static final String AMMO = "\u25FC";
    public static final char SPACE = ' ';
    public static final String DAMAGE_TOKEN = "\u25C6";
    public static final String MARK_TOKEN = "\u25CE";

    public synchronized static void concatRow(List<String> list1, List<String> list2){
        String s;
        for( int i = 0 ; i< list1.size() && i< list2.size(); i++)
        {
            s = list1.get(i);
            s = s.concat(list2.get(i));
            list1.remove(i);
            list1.add(i, s);
        }
    }

    public static String addChars(String out, char c, int n){
        while(n > 0){
            out = out.concat(""+c);
            n--;
        }
        return out;
    }

    public static String addSpace(String out, int space){
        return CharCli.addChars( out, CharCli.SPACE, space );
    }

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
