package it.polimi.ingsw.model;

import java.util.*;

public class AmmoTile {

    private List<AmmoColor> ammo;

    public AmmoTile(){
        ammo = new ArrayList<>();
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
