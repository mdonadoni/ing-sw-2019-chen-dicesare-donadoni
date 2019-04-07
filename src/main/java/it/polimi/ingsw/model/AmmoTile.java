package it.polimi.ingsw.model;

import java.util.*;

public class AmmoTile {

    private List<AmmoColor> ammo;

    public AmmoTile(AmmoColor ammoColor1, AmmoColor ammoColor2, AmmoColor ammoColor3){
        ammo = new ArrayList<>();
        ammo.add(ammoColor1);
        ammo.add(ammoColor2);
        ammo.add(ammoColor3);
    }


    public AmmoTile(AmmoColor ammoColor1, AmmoColor ammoColor2){
        ammo = new ArrayList<>();
        ammo.add(ammoColor1);
        ammo.add(ammoColor2);
     }

    public void addAmmo(AmmoColor ammoColor) {
        ammo.add(ammoColor);
    }

    public List<AmmoColor> getAmmo() {
        return this.ammo;
    }

    public boolean hasPoweUp(){
        return ammo.size()==2;
    }
}
