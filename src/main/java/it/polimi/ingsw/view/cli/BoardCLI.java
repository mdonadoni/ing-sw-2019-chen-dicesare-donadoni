package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.minified.*;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;

public class BoardCLI {
    public static final int LENGTH = 20;

    public synchronized ArrayList viewBoard(MiniBoard miniBoard) {
        String out = "";
        MiniSquare ms;
        ArrayList<String> outList = new ArrayList<>();
        ArrayList<String> tempList = new ArrayList<>();
        int r = 0;
        int c = 0;

        ArrayList<MiniSquare> squares = new ArrayList(miniBoard.getSpawnPoints());
        squares.addAll(miniBoard.getStandardSquares());

        ms = searchSquare(squares, new Coordinate(r, c));
        while (ms != null) {

            while (ms != null) {
                //square

                CharCli.concatRow(outList, tempList);

                tempList = viewSquare(ms);

                c++;
                ms = searchSquare(squares, new Coordinate(r, c));
            }
            outList.add(out);
            out = "";
            r++;
            c = 0;
            ms = searchSquare(squares, new Coordinate(r, c));
            while (ms == null) {
                c++;
                ms = searchSquare(squares, new Coordinate(r, c));
            }
        }
        fillCorner(outList);
        return outList;
    }

    public synchronized ArrayList viewSquare(MiniSquare ms) {
        ArrayList<String> outList = new ArrayList<>();
        String out = " ";
        int space;
        char top = ' ';
        char left = ' ';
        MiniStandardSquare mss;
        MiniSpawnPoint msp;

        //top
        if (ms.getBoundary(Cardinal.NORTH) == LinkType.WALL) {
            top = CharCli.HORIZONTAL_WALL;
        } else if (ms.getBoundary(Cardinal.NORTH) == LinkType.DOOR) {
            top = CharCli.HORIZONTAL_DOOR;
        } else if (ms.getBoundary(Cardinal.NORTH) == LinkType.SAME_ROOM) {
            top = CharCli.HORIZONTAL_SAME_ROOM;
        }
        out = CharCli.addChars(out, top, LENGTH);
        outList.add(out.concat(" "));

        //player

        if (ms.getBoundary(Cardinal.WEST) == LinkType.WALL) {
            left = CharCli.VERTICAL_WALL;
        } else if (ms.getBoundary(Cardinal.WEST) == LinkType.DOOR) {
            left = CharCli.VERTICAL_DOOR;
        } else if (ms.getBoundary(Cardinal.WEST) == LinkType.SAME_ROOM) {
            left = CharCli.VERTICAL_SAME_ROOM;
        }

        out = "" + left;
        for (PlayerToken pt : ms.getPlayers()) {
            out = out.concat(ColorCLI.getPlayerColor(pt, "P"));
        }
        space = LENGTH - ms.getPlayers().size();
        out = out.concat(CharCli.addSpace(out, space));
        outList.add(out);
        //empty rows
        for (int i = 0; i < LENGTH - 5; i++) {
            out = CharCli.addSpace(out, LENGTH);
            outList.add(out);
        }

        //weapons
        if (ms.getClass() == MiniSpawnPoint.class) {
            msp = (MiniSpawnPoint) ms;
            for (MiniWeapon mw : msp.getWeapons()) {
                out = "" + left;
                out = out.concat(mw.getName());
                space = LENGTH - mw.getName().length();
                out = CharCli.addSpace(out, space);
                outList.add(out);
            }
            //empty rows
            for (int n = 4 - msp.getWeapons().size(); n > 0; n--) {
                out = "" + left;
                out = CharCli.addSpace(out, LENGTH);
                outList.add(out);
            }
            out = ColorCLI.getAmmoColor(msp.getColor(), "SP");
            space = LENGTH - 2;
            out = CharCli.addSpace(out, space);
            outList.add(out);
        }//ammo tile
        else if (ms.getClass() == MiniStandardSquare.class) {
            mss = (MiniStandardSquare) ms;
            //empty rows
            for (int n = 3; n > 0; n--) {
                out = "" + left;
                out = CharCli.addSpace(out, LENGTH);
                outList.add(out);
            }
            out = "" + left;
            for (AmmoColor ac : mss.getAmmoTile().getAmmo()) {
                out = out.concat(ColorCLI.getAmmoColor(ac, CharCli.AMMO));
            }
            if (mss.getAmmoTile().hasPowerUp()) {
                out = out.concat("p");
                out = CharCli.addSpace(out, LENGTH - 3);
            } else
                out = CharCli.addSpace(out, LENGTH - 2);
            outList.add(out);
        }
        return outList;
    }

    public synchronized void fillCorner(ArrayList<String> list) {
        String[] aString = (String[]) list.toArray();
        char[] row;
        char[] up_row;
        char[] down_row;

        for (int i = 0; i < list.size(); i += LENGTH + 1) {
            for (int j = 0; i < aString[i].length(); j += LENGTH + 1) {

                row = aString[i].toCharArray();
                up_row = aString[i - 1].toCharArray();
                down_row = aString[i + 1].toCharArray();
                if (row[j] == ' ') {
                    //left
                    if (row[j - 1] == CharCli.HORIZONTAL_WALL ) {
                        //right
                        if (row[j + 1] == CharCli.HORIZONTAL_WALL || row[j + 1] == CharCli.HORIZONTAL_DOOR || row[j + 1] == CharCli.HORIZONTAL_SAME_ROOM) {
                            //top
                            if (up_row[j] == CharCli.VERTICAL_WALL ) {
                                //down
                                if (down_row[j] == CharCli.VERTICAL_WALL || down_row[j] == CharCli.VERTICAL_DOOR || down_row[j] == CharCli.VERTICAL_SAME_ROOM) {
                                    row[j] = CharCli.CROSS_CORNER_WALL;
                                } else {
                                    row[j] = CharCli.T_WALL_DOWN_CORNER;
                                }
                            }else if(up_row[j] == CharCli.VERTICAL_SAME_ROOM || up_row[j] == CharCli.VERTICAL_DOOR){
                                //down
                                if (down_row[j] == CharCli.VERTICAL_WALL || down_row[j] == CharCli.VERTICAL_DOOR || down_row[j] == CharCli.VERTICAL_SAME_ROOM) {
                                    row[j] = CharCli.CROSS_CORNER_WALL;
                                } else {
                                    row[j] = CharCli.T_DOWN_CORNER;
                                }
                            }
                            else {
                                //down
                                if (down_row[j] == CharCli.VERTICAL_WALL) {
                                    row[j] = CharCli.T_WALL_TOP_CORNER;
                                } else if (down_row[j] == CharCli.VERTICAL_DOOR ||
                                        down_row[j] == CharCli.VERTICAL_SAME_ROOM) {
                                    row[j] = CharCli.T_TOP_CORNER;
                                }
                            }
                        } else{
                            //top
                            if (up_row[j] == CharCli.VERTICAL_WALL || up_row[j] == CharCli.VERTICAL_DOOR || up_row[j] == CharCli.VERTICAL_SAME_ROOM) {
                                //down
                                if (down_row[j] == CharCli.VERTICAL_WALL || down_row[j] == CharCli.VERTICAL_DOOR || down_row[j] == CharCli.VERTICAL_SAME_ROOM) {
                                    row[j] = CharCli.T_WALL_LEFT;
                                } else {
                                    row[j] = CharCli.BOTTOM_RIGHT_CORNER;
                                }
                            }else {
                                //down
                                if (down_row[j] == CharCli.VERTICAL_WALL || down_row[j] == CharCli.VERTICAL_DOOR || down_row[j] == CharCli.VERTICAL_SAME_ROOM) {
                                    row[j] = CharCli.TOP_RIGHT_CORNER;
                                }
                            }

                        }

                    } else if (row[j - 1] == CharCli.HORIZONTAL_DOOR || row[j - 1] == CharCli.HORIZONTAL_SAME_ROOM){
                        //right
                        if ( row[j + 1] == CharCli.HORIZONTAL_DOOR || row[j + 1] == CharCli.HORIZONTAL_SAME_ROOM) {
                            //top
                            if ( up_row[j] == CharCli.VERTICAL_DOOR || up_row[j] == CharCli.VERTICAL_SAME_ROOM) {
                                //down
                                if ( down_row[j] == CharCli.VERTICAL_DOOR || down_row[j] == CharCli.VERTICAL_SAME_ROOM) {
                                    row[j] = CharCli.CROSS_CORNER;
                                } else if (down_row[j] == CharCli.VERTICAL_WALL ){
                                    row[j] = CharCli.CROSS_CORNER_WALL;
                                }
                            } else if( up_row[j] == CharCli.VERTICAL_WALL ){
                                //down
                                if (down_row[j] == CharCli.VERTICAL_WALL || down_row[j] == CharCli.VERTICAL_DOOR || down_row[j] == CharCli.VERTICAL_SAME_ROOM) {
                                    row[j] = CharCli.CROSS_CORNER_WALL;
                                }
                            }
                        } else if( row[j + 1] == CharCli.HORIZONTAL_WALL){
                            //top
                            if (up_row[j] == CharCli.VERTICAL_WALL || up_row[j] == CharCli.VERTICAL_DOOR || up_row[j] == CharCli.VERTICAL_SAME_ROOM) {
                                //down
                                if (down_row[j] == CharCli.VERTICAL_WALL || down_row[j] == CharCli.VERTICAL_DOOR || down_row[j] == CharCli.VERTICAL_SAME_ROOM) {
                                    row[j] = CharCli.CROSS_CORNER_WALL;
                                } else {
                                    row[j] = CharCli.T_WALL_DOWN_CORNER;
                                }
                            }else {
                                //down
                                if (down_row[j] == CharCli.VERTICAL_WALL || down_row[j] == CharCli.VERTICAL_DOOR || down_row[j] == CharCli.VERTICAL_SAME_ROOM) {
                                    row[j] = CharCli.T_TOP_CORNER;
                                }
                            }

                        }else{
                            //top
                            if (up_row[j] == CharCli.VERTICAL_WALL || up_row[j] == CharCli.VERTICAL_DOOR || up_row[j] == CharCli.VERTICAL_SAME_ROOM) {
                                //down
                                if (down_row[j] == CharCli.VERTICAL_WALL || down_row[j] == CharCli.VERTICAL_DOOR || down_row[j] == CharCli.VERTICAL_SAME_ROOM) {
                                    row[j] = CharCli.T_LEFT;
                                }
                            }
                        }
                    } else {
                        //right
                        if( row[j+1] == CharCli.HORIZONTAL_SAME_ROOM || row[j+1] == CharCli.HORIZONTAL_DOOR){
                            //top
                            if(up_row[j] == CharCli.VERTICAL_WALL || up_row[j] == CharCli.VERTICAL_DOOR || up_row[j] == CharCli.VERTICAL_SAME_ROOM){
                                //bottom
                                if(down_row[j] == CharCli.VERTICAL_WALL ||down_row[j] == CharCli.VERTICAL_SAME_ROOM || down_row[j] == CharCli.VERTICAL_DOOR ) {
                                    row[j] = CharCli.T_RIGHT;
                                }else{
                                    row[j] = CharCli.BOTTOM_LEFT_CORNER;
                                }
                            }
                        }
                        else if( row[j+1] == CharCli.HORIZONTAL_WALL){
                            //top
                            if(up_row[j] == CharCli.VERTICAL_WALL || up_row[j] == CharCli.VERTICAL_DOOR || up_row[j] == CharCli.VERTICAL_SAME_ROOM){
                                //bottom
                                if(down_row[j] == CharCli.VERTICAL_WALL ||down_row[j] == CharCli.VERTICAL_SAME_ROOM || down_row[j] == CharCli.VERTICAL_DOOR ) {
                                    row[j] = CharCli.T_WALL_RIGHT;
                                }else{
                                    row[j] = CharCli.TOP_LEFT_CORNER;
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    public synchronized MiniSquare searchSquare(ArrayList<MiniSquare> list, Coordinate coordinate) {
        for (MiniSquare ms : list) {
            if (coordinate.equals(ms.getCoordinates()))
                return ms;
        }
        return null;
    }

}