package it.polimi.ingsw.view.cli.component;

import it.polimi.ingsw.model.minified.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.cli.util.CharCli;
import it.polimi.ingsw.view.cli.util.ColorCLI;

import java.util.ArrayList;
import java.util.List;

public class BoardCLI {
    public static final int LENGTH = 15;

    private MiniBoard miniBoard;

    public BoardCLI(MiniBoard miniBoard){
        this.miniBoard=miniBoard;
    }

    public List<String> viewBoard() {
        MiniSquare ms;
        List<String> outList = new ArrayList<>();
        List<String> tempList1 = new ArrayList<>();
        List<String> tempList2 = new ArrayList<>();
        int r = 0;
        int c = 0;

        List<MiniSquare> squares = new ArrayList();
        squares.addAll(miniBoard.getSpawnPoints());
        squares.addAll(miniBoard.getStandardSquares());
        int nSquare = squares.size();
        ms = searchSquare(squares, new Coordinate(r, c));

        while (ms != null || nSquare-1>0) {
            if(ms==null) {
                tempList1.clear();
                for (int n = LENGTH/2+1; n > 0; n--) {
                    tempList1.add(CharCli.addSpace("", LENGTH+2));
                }
                c++;
                nSquare--;
                ms = searchSquare(squares, new Coordinate(r, c));
            }else {
                tempList1 = viewSquare(ms);
                c++;
                nSquare--;
                ms = searchSquare(squares, new Coordinate(r, c));
            }
            while (ms != null) {
                //square
                tempList2 = viewSquare(ms);
                CharCli.concatRow(tempList1, tempList2);
                nSquare--;
                c++;
                ms = searchSquare(squares, new Coordinate(r, c));
            }
            outList.addAll(tempList1);
            r++;
            c = 0;
            ms = searchSquare(squares, new Coordinate(r, c));

        }
        return outList;
    }

    public List<String> viewSquare(MiniSquare ms) {
        ArrayList<String> outList = new ArrayList<>();
        String out ;
        int space;
        char top ;
        char left;
        char right;
        char bottom;
        char leftCorner;
        char rightCorner;
        MiniStandardSquare mss;
        MiniSpawnPoint msp;
        //top
        leftCorner = CharCli.getCornerChar(ms, Cardinal.NORTH, Cardinal.WEST);
        top = CharCli.getLinkChar( ms, Cardinal.NORTH);
        left = CharCli.getLinkChar(ms, Cardinal.WEST);
        right = CharCli.getLinkChar(ms, Cardinal.EAST);
        rightCorner = CharCli.getCornerChar(ms, Cardinal.NORTH, Cardinal.EAST);
        out = ""+leftCorner;
        out = CharCli.addChars(out, top, LENGTH);
        out = out.concat(""+rightCorner);
        outList.add(out);
        //player
        out = "" + left;
        for (PlayerToken pt : ms.getPlayers()) {
            out = out.concat(ColorCLI.getPlayerColor(pt, "P"));
        }
        space = LENGTH - ms.getPlayers().size();
        out = CharCli.addSpace(out, space);
        out = out.concat(""+right);
        outList.add(out);
        //weapons
        if (ms.getClass() == MiniSpawnPoint.class) {
            msp = (MiniSpawnPoint) ms;
            for (MiniWeapon mw : msp.getWeapons()) {
                out = "" + left;
                out = out.concat(mw.getName());
                space = LENGTH - mw.getName().length();
                out = CharCli.addSpace(out, space);
                out = out.concat("" + right);
                outList.add(out);
            }
            //empty rows
            for (int n = 4 - msp.getWeapons().size(); n > 0; n--) {
                out = "" + left;
                out = CharCli.addSpace(out, LENGTH);
                out = out.concat("" + right);
                outList.add(out);
            }
            out = left+ ColorCLI.getAmmoColor(msp.getColor(), "SP");
            space = LENGTH - 2;
            out = CharCli.addSpace(out, space);
            out = out.concat("" + right);
            outList.add(out);
        }//ammo tile
        else if (ms.getClass() == MiniStandardSquare.class) {
            mss = (MiniStandardSquare) ms;
            //empty rows
            for (int n = 4; n > 0; n--) {
                out = "" + left;
                out = CharCli.addSpace(out, LENGTH);
                out = out.concat("" + right);
                outList.add(out);
            }
            out = "" + left;
            if (mss.hasAmmo()) {
                for (AmmoColor ac : mss.getAmmoTile().getAmmo()) {
                    out = out.concat(ColorCLI.getAmmoColor(ac, CharCli.AMMO));
                }
                if (mss.getAmmoTile().hasPowerUp()) {
                    out = out.concat("p-u");
                    out = CharCli.addSpace(out, LENGTH - 5);
                } else {
                    out = CharCli.addSpace(out, LENGTH - 3);
                }
            } else {
                out = CharCli.addSpace(out, LENGTH);
            }
            out = out.concat("" + right);
            outList.add(out);
        }
        //bottom
        leftCorner = CharCli.getCornerChar(ms, Cardinal.SOUTH, Cardinal.WEST);
        bottom = CharCli.getLinkChar( ms, Cardinal.SOUTH);
        rightCorner = CharCli.getCornerChar(ms, Cardinal.SOUTH, Cardinal.EAST);
        out = "" + leftCorner;
        out = CharCli.addChars(out, bottom, LENGTH);
        out = out.concat("" + rightCorner);
        outList.add(out);
        return outList;
    }

    private  MiniSquare searchSquare(List<MiniSquare> list, Coordinate coordinate) {
        for (MiniSquare ms : list) {
            if (coordinate.equals(ms.getCoordinates()))
                return ms;
        }
        return null;
    }
}