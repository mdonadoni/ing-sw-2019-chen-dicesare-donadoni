package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.minified.MiniPlayer;
import it.polimi.ingsw.model.minified.MiniWeapon;
import java.util.ArrayList;
import java.util.List;

public class PlayerCLI {

    private static final int LENGTH = 15 ;

    public static synchronized ArrayList viewPlayer(MiniPlayer miniPlayer, List<PowerUp> powerUps){
        int space;
        String out;
        ArrayList<String> outList = new ArrayList<>();
        out = ColorCLI.getPlayerColor(miniPlayer.getColor(), miniPlayer.getNickname());
        space = LENGTH - miniPlayer.getNickname().length() +2;
        //out = String.format("%-15s", out);
        out = CharCli.addSpace(out,space);
        outList.add(out);
        //top
        out = ""+CharCli.TOP_LEFT_CORNER;
        out = CharCli.addChars(out, CharCli.HORIZONTAL_WALL, LENGTH);
        out = out.concat(""+CharCli.TOP_RIGHT_CORNER);
        outList.add(out);
        //marks
        out=""+CharCli.VERTICAL_WALL;
        for( PlayerToken pt : miniPlayer.getMarks() ) {

            out = out.concat(ColorCLI.getPlayerColor(pt, CharCli.MARK_TOKEN));
        }
        space = LENGTH - miniPlayer.getMarks().size();
        //out = String.format("%-15s", out);
        out = CharCli.addSpace(out,space);
        out = out.concat(""+CharCli.VERTICAL_WALL);
        outList.add(out);
        //damage
        out=""+CharCli.VERTICAL_WALL;
        for( PlayerToken pt : miniPlayer.getDamageTaken()){
            out = out.concat(ColorCLI.getPlayerColor(pt, CharCli.DAMAGE_TOKEN));
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
        out=""+CharCli.VERTICAL_WALL;
        space = miniPlayer.getAmmo().size();
        for(AmmoColor ac : miniPlayer.getAmmo()){
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
        out="";
        for(MiniWeapon wp : miniPlayer.getWeapons()){
            out=wp.getName();
            space = out.length();
            out = out.concat("("+ColorCLI.getAmmoColor(wp.getAdditionalRechargeColor(), CharCli.AMMO)+")");
            space += 3;
            for ( AmmoColor ac : wp.getPickupColor()){
                out = out.concat(ColorCLI.getAmmoColor(ac, CharCli.AMMO));
                space++;
            }
            space = LENGTH - space +2;
            out = CharCli.addSpace(out, space);
            outList.add(out);
        }
        //powerup
        if(powerUps!=null) {
            for (PowerUp p : powerUps) {
                out = ColorCLI.getAmmoColor(p.getAmmo(), p.getType().toString());
                outList.add(out);
            }
        }
        return outList;
    }
/*
    public static void main(String[] args){
        Player p = new Player("a", PlayerToken.PURPLE);
        p.addMark(PlayerToken.YELLOW,2);
        p.addDamage(PlayerToken.GREY, 3);
        p.addDamage(PlayerToken.BLUE,1);

        Weapon w1 =new Weapon("Vortex");
        w1.setAdditionalRechargeColor(AmmoColor.RED);
        w1.addPickupColor(AmmoColor.YELLOW);
        w1.addPickupColor(AmmoColor.BLUE);
        p.grabWeapon(w1);
        p.grabWeapon(new Weapon("Flamethrower"));
        p.grabWeapon(new Weapon("Laserblade"));
        p.addPowerUp(new PowerUp(PowerUpType.TELEPORTER, AmmoColor.RED));
        p.grabAmmo(new AmmoTile(AmmoColor.YELLOW, AmmoColor.YELLOW, AmmoColor.BLUE));
        MiniPlayer mp = new MiniPlayer(p);

        ArrayList<String> out = PlayerCLI.viewPlayer(mp, p.getPowerUps());
        for (String s : out){
            System.out.println(s);
        }
    }
*/
}
