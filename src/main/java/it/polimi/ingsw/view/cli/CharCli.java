package it.polimi.ingsw.view.cli;

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
    public static final char CROSS_CORNER='\u253C';
    public static final char T_WALL_TOP_CORNER='\u2566';
    public static final char T_WALL_DOWN_CORNER='\u2569';
    public static final char T_TOP_CORNER='\u2564';
    public static final char T_DOWN_CORNER='\u2567';
    public static final char T_WALL_LEFT='\u2563';
    public static final char T_WALL_RIGHT='\u2560';
    public static final char T_LEFT='\u2562';
    public static final char T_RIGHT='\u255F';
    public static final String SKULL = "\u2620";
    public static final String AMMO = "\u25FC";


    public static void concatRow(List<String> list1, List<String> list2){
        int i;
        for( String s : list1 ){
            i = list1.indexOf(s);
            s.concat(list2.get(i));
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
        return addChars( out,' ', space );
    }

}
