package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.minified.*;
import it.polimi.ingsw.model.*;
import java.util.ArrayList;
import java.util.List;

public class BoardCLI {
    public static final int LENGTH = 15;

    public synchronized static List viewBoard(MiniBoard miniBoard) {
        MiniSquare ms;
        ArrayList<String> outList = new ArrayList<>();
        ArrayList<String> tempList1 = new ArrayList<>();
        ArrayList<String> tempList2 = new ArrayList<>();
        int r = 0;
        int c = 0;

        ArrayList<MiniSquare> squares = new ArrayList();
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
                tempList1 = (ArrayList<String>) viewSquare(ms);
                c++;
                nSquare--;
                ms = searchSquare(squares, new Coordinate(r, c));
            }
            while (ms != null) {
                //square
                tempList2 = (ArrayList<String>) viewSquare(ms);
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

    public static synchronized List viewSquare(MiniSquare ms) {
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
            for (AmmoColor ac : mss.getAmmoTile().getAmmo()) {
                out = out.concat(ColorCLI.getAmmoColor(ac, CharCli.AMMO));
            }
            if (mss.getAmmoTile().hasPowerUp()) {
                out = out.concat("p-u");
                out = CharCli.addSpace(out, LENGTH - 5);
            } else
                out = CharCli.addSpace(out, LENGTH - 3);
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

    public static synchronized MiniSquare searchSquare(ArrayList<MiniSquare> list, Coordinate coordinate) {
        for (MiniSquare ms : list) {
            if (coordinate.equals(ms.getCoordinates()))
                return ms;
        }
        return null;
    }

    public static synchronized List viewGameBoard(MiniGameBoard gameBoard){
        ArrayList<String> outLIst = new ArrayList<>();
        String out=" ";
        PlayerToken pt;
        //player kill shot track
        ArrayList<ArrayList<PlayerToken>> killShotTrack = gameBoard.getKillShotTrack();
        for(ArrayList<PlayerToken> kills : killShotTrack){
            if(gameBoard.getRemainingSkulls()>0){
                pt = kills.get(0);
                if(kills.size()>1) {
                    out = out.concat(" +"+ColorCLI.getPlayerColor(pt, CharCli.DAMAGE_TOKEN));
                }else {
                    out = out.concat(" "+ColorCLI.getPlayerColor(pt, CharCli.DAMAGE_TOKEN));
                }
            }else{
                out = out.concat("|");
                for(PlayerToken p : kills){
                    out = out.concat(ColorCLI.getPlayerColor(p, CharCli.DAMAGE_TOKEN));
                }
            }
        }
        //remain skulls
        for (int i=gameBoard.getRemainingSkulls(); i>0 ; i--){
            out = out.concat(" "+ColorCLI.turnRed(CharCli.SKULL));
        }
        outLIst.add(out);
        //board
        outLIst.addAll(viewBoard(gameBoard.getBoard()));
        return outLIst;
    }

    public static synchronized List viewMatch(MiniMatch match){
        ArrayList<String> outList = new ArrayList<>();
        ArrayList<String> squareList = new ArrayList<>();
        String out;
        //current turn
        out = "Turno di : " +match.getCurrentTurn().getCurrentPlayer();
        squareList.add(out);
        //players
        outList.add("");
        outList.add("");
        for( MiniPlayer p : match.getPlayers()){
            outList.addAll(PlayerCLI.viewPlayer(p,null));
        }
        //gameboard
        squareList.add("");
        squareList.addAll(viewGameBoard(match.getGameBoard()));

        CharCli.concatRow( outList ,squareList);
        return outList;
    }

    public static synchronized List viewModel(MiniModel model){
        ArrayList<String> outList = new ArrayList<>();
        String out;
        //match
        outList.addAll(viewMatch(model.getMatch()));
        //player
        outList.addAll(PlayerCLI.viewPlayer(model.getMyMiniPlayer(), model.getMyPowerUps()));
        out="Punti : "+model.getMyPoints();
        outList.add(out);
        return outList;
    }
/*
    public static void main (String[] args){
        Match match = new Match(
                Arrays.asList("Sim", "Mar", "Fed", "D", "E"),
                new JsonModelFactory(BoardType.SMALL)
        );
        Player pA = match.getPlayerByNickname("Sim");
        Player pB = match.getPlayerByNickname("Mar");
        Player pC = match.getPlayerByNickname("Fed");
        Player pD = match.getPlayerByNickname("D");
        Player pE = match.getPlayerByNickname("E");
        pA.addMark(PlayerToken.BLUE, 2);
        pA.addDamage(PlayerToken.YELLOW, 5);
        pA.addPowerUp(new PowerUp(PowerUpType.NEWTON, AmmoColor.RED));
        Weapon w1 =new Weapon("Vortex");
        w1.setAdditionalRechargeColor(AmmoColor.RED);
        w1.addPickupColor(AmmoColor.YELLOW);
        w1.addPickupColor(AmmoColor.BLUE);
        pA.grabWeapon(w1);

        MiniMatch miniMatch = new MiniMatch(match);
        MiniModel miniModel = new MiniModel(match,match.getPlayerByNickname("Sim"));


        ArrayList<String> s = (ArrayList<String>) BoardCLI.viewModel(miniModel);
        for( String sa : s){
            System.out.println(sa);
        }
    }

 */
}