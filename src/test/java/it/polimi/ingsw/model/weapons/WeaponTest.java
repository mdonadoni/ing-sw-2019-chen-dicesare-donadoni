package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.AmmoColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeaponTest {

    @Test
    public void fromResourceTest() {
        Weapon wp = new Weapon(WeaponTest.class.getResourceAsStream("/weapons/testweapon.json"));
        assertEquals(wp.getName(), "testweapon");
        assertEquals(wp.getAdditionalRechargeColor(), AmmoColor.BLUE);
        assertEquals(wp.getPickupColor().get(0), AmmoColor.BLUE);
        assertEquals(wp.getPickupColor().get(1), AmmoColor.RED);
        Attack att = wp.getAttacks().get(0);
        assertFalse(att.getChainAttack());
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
        assertEquals(eff0.getAmount(), 2);
        assertEquals(eff1.getType(), HarmType.MARK);
        assertEquals(eff1.getAmount(), 1);
        assertEquals(eff2.getAmount(), 3);
        assertTrue(eff2.isLine());
        assertEquals(att.getAdditionalAttacks().get(0).getCost().get(0), AmmoColor.RED);
    }

    @Test
    public void fromResource(){
        Weapon wp = new Weapon(Weapon.class.getResourceAsStream("/weapons/lockrifle.json"));
        assertEquals(wp.getPickupColor().get(0), AmmoColor.BLUE);
        wp = new Weapon(Weapon.class.getResourceAsStream("/weapons/electroscythe.json"));
        wp = new Weapon(Weapon.class.getResourceAsStream("/weapons/machinegun.json"));
        wp = new Weapon(Weapon.class.getResourceAsStream("/weapons/tractorbeam.json"));
        wp = new Weapon(Weapon.class.getResourceAsStream("/weapons/thor.json"));
        wp = new Weapon(Weapon.class.getResourceAsStream("/weapons/plasmagun.json"));
        wp = new Weapon(Weapon.class.getResourceAsStream("/weapons/whisper.json"));
        wp = new Weapon(Weapon.class.getResourceAsStream("/weapons/vortexcannon.json"));
        wp = new Weapon(Weapon.class.getResourceAsStream("/weapons/furnace.json"));

    }
}