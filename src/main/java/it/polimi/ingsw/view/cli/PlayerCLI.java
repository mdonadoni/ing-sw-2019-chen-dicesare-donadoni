package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.PlayerToken;
import it.polimi.ingsw.model.minified.MiniPlayer;

import javax.security.auth.callback.CallbackHandler;
import java.util.ArrayList;

public class PlayerCLI {

    private static final int LENGTH = 15 ;
    private static final int HEIGTH = 15 ;
    private static final String DAMAGE_TOKEN = "\u25C6";
    private static final String MARK_TOKEN = "\u25CE";

    public synchronized ArrayList viewPlayer(MiniPlayer miniPlayer){
        int space;
        String out = "";
        ArrayList<String> outList = new ArrayList<>();
        out = out.concat(ColorCLI.getPlayerColor(miniPlayer.getColor(), miniPlayer.getNickname())+"\n");
        out = out.concat(""+CharCli.TOP_LEFT_CORNER);
        out = CharCli.addChars(out, CharCli.HORIZONTAL_WALL, LENGTH);
        out = out.concat(""+CharCli.TOP_RIGHT_CORNER);
        outList.add(out);
        //marks
        out=""+CharCli.TOP_LEFT_CORNER;
        for( PlayerToken pt : miniPlayer.getMarks() ) {
            out = out.concat(ColorCLI.getPlayerColor(pt, MARK_TOKEN));
        }
        space = LENGTH - miniPlayer.getMarks().size();
        out = CharCli.addSpace(out,space);
        out = out.concat(""+CharCli.VERTICAL_WALL);
        outList.add(out);
        //damage
        out=""+CharCli.VERTICAL_WALL;
        for( PlayerToken pt : miniPlayer.getDamageTaken()){
            out = out.concat(ColorCLI.getPlayerColor(pt, DAMAGE_TOKEN));
        }
        space = 12 - miniPlayer.getDamageTaken().size();
        out = CharCli.addSpace(out, space);
        out = out.concat("   "+ CharCli.VERTICAL_WALL);
        outList.add(out);
        //skulls
        out=""+CharCli.VERTICAL_WALL;
        space = miniPlayer.getSkulls();
        for (int i=space ; i>0 ; i--){
            out = out.concat(CharCli.SKULL);
        }
        space = LENGTH - space ;
        out = CharCli.addSpace(out, space);
        out = out.concat(""+CharCli.VERTICAL_WALL);
        outList.add(out);
        //ammo
        out="";
        space = miniPlayer.getAmmo().size();
        for(AmmoColor ac : miniPlayer.getAmmo()){
            out = out.concat(ColorCLI.getAmmoColor(ac,CharCli.AMMO));
        }
        space = LENGTH - space ;
        out = CharCli.addSpace(out, space);
        out = out.concat(""+ CharCli.VERTICAL_WALL);
        outList.add(out);
        //weapon

        //powerup


        return outList;
    }

}
