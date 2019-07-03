package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeaponTest {

    @Test
    public void fromResourceTest() {
        /*Weapon wp = new Weapon(WeaponTest.class.getResourceAsStream("/weapons/testweapon.json"));
        assertEquals(wp.getName(), "testweapon");
        assertEquals(wp.getAdditionalRechargeColor(), AmmoColor.BLUE);
        assertEquals(wp.getPickupColor().get(0), AmmoColor.BLUE);
        assertEquals(wp.getPickupColor().get(1), AmmoColor.RED);
        Attack att = wp.getAttacks().get(0);
        assertEquals(att.getCost().size(), 2);
        assertEquals(att.getCost().get(0), AmmoColor.YELLOW);
        PlayerTarget plTg = (PlayerTarget)att.getBaseFire().get(0);
        assertEquals(plTg.getVisibility(), Visibility.VISIBLE);
        assertEquals(plTg.getMinDistance(), -1);
        assertEquals(plTg.getMaxDistance(), -1);
        assertEquals(plTg.getSpecial().size(), 0);
        HarmfulEffect eff0 = (HarmfulEffect)plTg.getEffects().get(0);
        HarmfulEffect eff1 = (HarmfulEffect)plTg.getEffects().get(1);
        MovementEffect eff2 = (MovementEffect)plTg.getEffects().get(2);
        assertEquals(eff0.getType(), HarmType.DAMAGE);
        assertEquals(eff0.getValue(), 2);
        assertEquals(eff1.getType(), HarmType.MARK);
        assertEquals(eff1.getValue(), 1);
        assertEquals(eff2.getValue(), 3);
        assertTrue(eff2.isLine());
        assertEquals(att.getAdditionalAttacks().get(0).getCost().get(0), AmmoColor.RED);
    */
    }

    @Test
    public void fromResource(){
        JsonWeaponFactory weaponFactory = new JsonWeaponFactory();

        Weapon wp = weaponFactory.createWeapon(WeaponType.ELECTROSCYTHE);

    }
}