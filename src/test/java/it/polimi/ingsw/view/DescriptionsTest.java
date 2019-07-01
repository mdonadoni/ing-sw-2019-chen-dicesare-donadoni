package it.polimi.ingsw.view;

import it.polimi.ingsw.model.minified.MiniWeapon;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.model.weapons.WeaponType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DescriptionsTest {
    @Test
    void loadAttacks() {
        Weapon w = new Weapon(WeaponType.CYBERBLADE);
        String uuid = w.getAttacks().get(0).getUuid();
        assertTrue(Descriptions.find(new MiniWeapon(w), uuid).length() > 0);
    }
}