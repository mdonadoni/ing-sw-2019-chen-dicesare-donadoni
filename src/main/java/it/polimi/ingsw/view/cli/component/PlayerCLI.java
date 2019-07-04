package it.polimi.ingsw.view.cli.component;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.PlayerToken;
import it.polimi.ingsw.model.minified.MiniPlayer;
import it.polimi.ingsw.model.minified.MiniPowerUp;
import it.polimi.ingsw.model.minified.MiniWeapon;
import it.polimi.ingsw.view.cli.util.CharCli;
import it.polimi.ingsw.view.cli.util.ColorCLI;

import java.util.ArrayList;
import java.util.List;

public class PlayerCLI {

    static final int LENGTH = 25;

    private MiniPlayer player;
    private List<MiniPowerUp> powerUp;
    private ArrayList<WeaponCLI> weaponCLI;

    PlayerCLI(MiniPlayer miniPlayer, List<MiniPowerUp> powerUp){
        this.player = miniPlayer;
        this.powerUp = powerUp;
        weaponCLI = new ArrayList<>();
        for(MiniWeapon w : miniPlayer.getWeapons()){
            weaponCLI.add( new WeaponCLI(w));
        }
    }

    public synchronized List<String> viewPlayer(){
        int space;
        String out;
        ArrayList<String> outList = new ArrayList<>();
        out = ColorCLI.getPlayerColor(player.getColor(), player.getNickname());
        space = LENGTH - player.getNickname().length() +2;
        out = CharCli.addSpace(out,space);
        outList.add(out);
        //top
        out = ""+CharCli.TOP_LEFT_CORNER;
        out = CharCli.addChars(out, CharCli.HORIZONTAL_WALL, LENGTH);
        out = out.concat(""+CharCli.TOP_RIGHT_CORNER);
        outList.add(out);
        //marks
        out=""+CharCli.VERTICAL_WALL;
        for( PlayerToken pt : player.getMarks() ) {

            out = out.concat(ColorCLI.getPlayerColor(pt, CharCli.MARK_TOKEN));
        }
        space = LENGTH - player.getMarks().size();
        out = CharCli.addSpace(out,space);
        out = out.concat(""+CharCli.VERTICAL_WALL);
        outList.add(out);
        //damage
        out=""+CharCli.VERTICAL_WALL;
        for( PlayerToken pt : player.getDamageTaken()){
            out = out.concat(ColorCLI.getPlayerColor(pt, CharCli.DAMAGE_TOKEN));
        }
        space = LENGTH - player.getDamageTaken().size();
        out = CharCli.addSpace(out, space);
        out = out.concat(Character.toString(CharCli.VERTICAL_WALL));
        outList.add(out);
        //skulls
        out=""+CharCli.VERTICAL_WALL;
        space = player.getSkulls();
        for (int i=space ; i>0 ; i--){
            out = out.concat(CharCli.SKULL);
        }
        space = LENGTH - space ;
        out = CharCli.addSpace(out, space);
        out = out.concat(""+CharCli.VERTICAL_WALL);
        outList.add(out);
        //ammo
        out=""+CharCli.VERTICAL_WALL;
        space = player.getAmmo().size();
        for(AmmoColor ac : player.getAmmo()){
            out = out.concat(ColorCLI.getAmmoColor(ac,CharCli.AMMO));
        }
        space = LENGTH - space ;
        out = CharCli.addSpace(out, space);
        out = out.concat(""+ CharCli.VERTICAL_WALL);
        outList.add(out);
        //bottom
        out = ""+CharCli.BOTTOM_LEFT_CORNER;
        out = CharCli.addChars(out, CharCli.HORIZONTAL_WALL, LENGTH);
        out = out.concat(""+CharCli.BOTTOM_RIGHT_CORNER);
        outList.add(out);
        //weapon
        for(WeaponCLI w : weaponCLI){
            out = w.viewWeapon().get(0);
            outList.add(out);
        }
        //powerup
        if(powerUp!=null) {
            for (MiniPowerUp p : powerUp) {
                out = ColorCLI.getAmmoColor(p.getAmmo(), p.getType().toString());
                outList.add(out);
            }
        }
        return outList;
    }
}
