package it.polimi.ingsw.view.cli.component;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.minified.MiniWeapon;
import it.polimi.ingsw.view.cli.util.CharCli;
import it.polimi.ingsw.view.cli.util.ColorCLI;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent the weapon in the CLI
 */
public class WeaponCLI {
    /**
     * The weapon to represent
     */
    private MiniWeapon miniWeapon;
    /**
     * Length of the space
     */
    private static final int LENGTH = 25 ;

    /**
     * Constructor
     * @param miniWeapon the weapon to represent
     */
    public WeaponCLI(MiniWeapon miniWeapon) {
        this.miniWeapon = miniWeapon;
    }
    /**
     * Generate the list of strings that represent the weapon
     * @return list of strings that represent the weapon
     */
    public List<String> viewWeapon(){
        String out;
        int space;
        List<String> outList = new ArrayList<>();
        out = miniWeapon.getName();
        space = miniWeapon.getName().length();
        if (!miniWeapon.isCharged()) {
            out = ColorCLI.turnStrikethrough(out);
        }
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
