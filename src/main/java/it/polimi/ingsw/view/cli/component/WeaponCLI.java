package it.polimi.ingsw.view.cli.component;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.minified.MiniWeapon;
import it.polimi.ingsw.view.cli.util.CharCli;
import it.polimi.ingsw.view.cli.util.ColorCLI;

import java.util.ArrayList;
import java.util.List;

public class WeaponCLI {
    private MiniWeapon miniWeapon;
    private static final int LENGTH = 15 ;

    public WeaponCLI(MiniWeapon miniWeapon) {
        this.miniWeapon = miniWeapon;
    }

    public List<String> viewWeapon(){
        String out;
        int space;
        List<String> outList = new ArrayList<>();
        out=miniWeapon.getName();
        space = out.length();
        out = out.concat("("+ ColorCLI.getAmmoColor(miniWeapon.getAdditionalRechargeColor(), CharCli.AMMO)+")");
        space += 3;
        for ( AmmoColor ac : miniWeapon.getPickupColor()){
            out = out.concat(ColorCLI.getAmmoColor(ac, CharCli.AMMO));
        }
        space = space + miniWeapon.getPickupColor().size();
        space = LENGTH - space +2;
        out = CharCli.addSpace(out, space);
        outList.add(out);
        return outList;
    }
}
