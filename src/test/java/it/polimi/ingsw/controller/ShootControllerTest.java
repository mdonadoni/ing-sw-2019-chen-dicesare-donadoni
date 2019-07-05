package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.weapons.Attack;
import it.polimi.ingsw.model.weapons.PlayerTarget;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.model.weapons.WeaponType;
import it.polimi.ingsw.network.TestView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ShootControllerTest {

    private Weapon weapon;
    private Match match;
    private Map<String, RemotePlayer> users;
    private TestView adaView;
    private Player ada;
    private Player bruce;
    private Player charlie;
    private Player daniel;
    private ShootController controller;
    private Board board;
    private JsonWeaponFactory weaponFactory;

    @BeforeEach
    void setup(){
        weaponFactory = new JsonWeaponFactory();
        List<String> nicks = new ArrayList<String>(Arrays.asList("Ada", "Bruce", "Charlie", "Daniel"));
        match = new Match(nicks, new JsonModelFactory(BoardType.MEDIUM_1));
        users = new HashMap<>();
        for(String nick : nicks){
            users.put(nick, new RemotePlayer(nick, new TestView()));
        }
        adaView = (TestView) users.get("Ada").getView();
        ada = match.getPlayerByNickname("Ada");
        bruce = match.getPlayerByNickname("Bruce");
        charlie = match.getPlayerByNickname("Charlie");
        daniel = match.getPlayerByNickname("Daniel");
        controller = new ShootController(users, match);
        board = match.getGameBoard().getBoard();

        users.get("Ada").setTimeLeft(5000);
    }


    @Test
    void lockrifleTest() throws RemoteException {
        weapon = weaponFactory.createWeapon(WeaponType.LOCKRIFLE);
        ada.grabWeapon(weapon);
        ada.move(board.getSquare(new Coordinate(1, 1)));
        bruce.move(board.getSquare(new Coordinate(1, 2)));
        charlie.move(board.getSquare(new Coordinate(1, 0)));
        daniel.move(board.getSquare(new Coordinate(0, 2)));

        ada.addAmmo(AmmoColor.RED);

        Attack attack = weapon.getAttacks().get(0);
        PlayerTarget rifleTarget = (PlayerTarget) attack.getBaseFire().get(0);

        controller.initShoot();
        List<Player> hittable = controller.getHittableEnemies(ada, rifleTarget);

        assertTrue(hittable.contains(bruce));
        assertTrue(hittable.contains(daniel));
        assertFalse(hittable.contains(charlie));

        // Inject all the choices
        // There's only 1 attack so there's no need to inject it, it will be automatically selected
        adaView.addSelectable(daniel.getUuid()); // Hit Daniel
        adaView.addSelectable(attack.getAdditionalAttacks().get(0).getUuid());
        adaView.addSelectable(bruce.getUuid());

        controller.shoot(ada, weapon);

        assertFalse(weapon.isCharged());
        assertEquals(daniel.getDamageTaken().size(), 2);
        assertEquals(daniel.getMarks().size(), 1);
        assertEquals(daniel.getDamageTaken().get(1), ada.getColor());
        assertEquals(daniel.getMarks().get(0), ada.getColor());
        assertEquals(bruce.getDamageTaken().size(), 0);
        assertEquals(bruce.getMarks().size(), 1);
        assertEquals(bruce.getMarks().get(0), ada.getColor());
    }

    @Test
    void tractorbeamTestBase() throws RemoteException{
        weapon = weapon = weaponFactory.createWeapon(WeaponType.TRACTORBEAM);
        ada.grabWeapon(weapon);
        ada.move(board.getSquare(new Coordinate(2, 2)));
        bruce.move(board.getSquare(new Coordinate(1, 2)));
        charlie.move(board.getSquare(new Coordinate(1, 0)));
        daniel.move(board.getSquare(new Coordinate(2, 3)));

        // Inject the choices
        adaView.addSelectable(daniel.getUuid());
        adaView.addSelectable(board.getSquare(new Coordinate(2, 1)).getUuid());

        controller.shoot(ada, weapon);

        assertEquals(daniel.getSquare(), board.getSquare(new Coordinate(2, 1)));
        assertEquals(daniel.getMarks().size(), 0);
        assertEquals(daniel.getDamageTaken().size(), 1);
        assertEquals(daniel.getDamageTaken().get(0), ada.getColor());
        assertFalse(weapon.isCharged());
    }

    @Test
    void tractorbeamTestCosty() throws RemoteException{
        weapon = weapon = weaponFactory.createWeapon(WeaponType.TRACTORBEAM);
        ada.grabWeapon(weapon);
        ada.move(board.getSquare(new Coordinate(2, 2)));
        bruce.move(board.getSquare(new Coordinate(1, 2)));
        charlie.move(board.getSquare(new Coordinate(1, 0)));
        daniel.move(board.getSquare(new Coordinate(2, 3)));

        ada.addAmmo(AmmoColor.RED);
        ada.addAmmo(AmmoColor.YELLOW);
        ada.addAmmo(AmmoColor.BLUE);

        // Inject choices
        adaView.addSelectable(weapon.getAttacks().get(1).getUuid());
        adaView.addSelectable(daniel.getUuid());

        controller.shoot(ada, weapon);

        assertEquals(daniel.getSquare(), ada.getSquare());
        assertEquals(daniel.getMarks().size(), 0);
        assertEquals(daniel.getDamageTaken().size(), 3);
        assertEquals(daniel.getDamageTaken().get(0), ada.getColor());
        assertFalse(weapon.isCharged());
        assertEquals(ada.getAmmo().size(), 1);
    }

    @Test
    void tractorBeamCostyNotVisible() throws RemoteException{
        weapon = weapon = weaponFactory.createWeapon(WeaponType.TRACTORBEAM);
        ada.grabWeapon(weapon);
        ada.move(board.getSquare(new Coordinate(0, 0)));
        bruce.move(board.getSquare(new Coordinate(1, 2)));
        charlie.move(board.getSquare(new Coordinate(1, 1)));
        daniel.move(board.getSquare(new Coordinate(2, 3)));

        ada.addAmmo(AmmoColor.RED);
        ada.addAmmo(AmmoColor.YELLOW);
        ada.addAmmo(AmmoColor.BLUE);

        // Shoot at Charlie using the costy Tractor Beam attack
        adaView.addSelectable(weapon.getAttacks().get(1).getUuid());
        adaView.addSelectable(charlie.getUuid());

        controller.shoot(ada, weapon);

        assertEquals(ada.getSquare(), charlie.getSquare());
        assertEquals(charlie.getDamageTaken().size(), 3);
        assertEquals(bruce.getSquare(), board.getSquare(new Coordinate(1, 2)));
        assertEquals(ada.getSquare(), board.getSquare(new Coordinate(0,0)));
        assertEquals(daniel.getSquare(), board.getSquare(new Coordinate(2, 3)));
    }

    @Test
    void electroscytheTest() throws RemoteException{
        weapon = weapon = weaponFactory.createWeapon(WeaponType.ELECTROSCYTHE);
        ada.grabWeapon(weapon);
        ada.move(board.getSquare(new Coordinate(2, 2)));
        bruce.move(board.getSquare(new Coordinate(1, 2)));
        charlie.move(board.getSquare(new Coordinate(2, 2)));
        daniel.move(board.getSquare(new Coordinate(2, 2)));

        ada.addAmmo(AmmoColor.BLUE);
        ada.addAmmo(AmmoColor.RED);

        // Inject choices
        adaView.addSelectable(weapon.getAttacks().get(1).getUuid());

        controller.shoot(ada, weapon);

        assertFalse(weapon.isCharged());
        assertEquals(charlie.getDamageTaken().size(), 2);
        assertEquals(charlie.getDamageTaken().get(0), ada.getColor());
        assertEquals(daniel.getDamageTaken().size(), 2);
        assertEquals(daniel.getDamageTaken().get(0), ada.getColor());
    }

    @Test
    void plasmagunTest() throws RemoteException{
        weapon = weapon = weaponFactory.createWeapon(WeaponType.PLASMAGUN);
        ada.grabWeapon(weapon);
        ada.move(board.getSquare(new Coordinate(0, 2)));
        bruce.move(board.getSquare(new Coordinate(1, 2)));
        charlie.move(board.getSquare(new Coordinate(1, 0)));
        daniel.move(board.getSquare(new Coordinate(2, 3)));

        // Inject Choices
        adaView.addSelectable(weapon.getAttacks().get(0).getBonusMovement().get(0).getUuid());
        adaView.addSelectable(board.getSquare(new Coordinate(0,1)).getUuid());
        adaView.addSelectable(charlie.getUuid());

        controller.shoot(ada, weapon);

        assertEquals(ada.getSquare(), board.getSquare(new Coordinate(0,1)));
        assertEquals(charlie.getDamageTaken().size(), 2);
        assertEquals(charlie.getMarks().size(), 0);
        assertEquals(charlie.getSquare(),board.getSquare(new Coordinate(1, 0)));
    }

    @Test
    void machinegunTest() throws RemoteException{
        weapon = weapon = weaponFactory.createWeapon(WeaponType.MACHINEGUN);
        ada.grabWeapon(weapon);
        ada.move(board.getSquare(new Coordinate(1, 1)));
        bruce.move(board.getSquare(new Coordinate(1, 2)));
        charlie.move(board.getSquare(new Coordinate(0, 1)));
        daniel.move(board.getSquare(new Coordinate(2, 2)));

        // Inject selection
        adaView.addSelectable(Arrays.asList(bruce.getUuid(), charlie.getUuid()));

        controller.initShoot();
        controller.applyTargets(ada, weapon.getAttacks().get(0));
        Attack additionalA = weapon.getAttacks().get(0).getAdditionalAttacks().get(0); // Attack that adds one damage
        Attack additionalB = weapon.getAttacks().get(0).getAdditionalAttacks().get(1); // Attack that adds one damage and shots a third target
        PlayerTarget target = (PlayerTarget) additionalB.getBaseFire().get(1);
        List<Player> hittableA = controller.getHittableEnemies(ada, (PlayerTarget) additionalA.getBaseFire().get(0));
        List<Player> hittableB = controller.getHittableEnemies(ada, target);

        assertTrue(hittableA.contains(bruce));
        assertTrue(hittableA.contains(charlie));
        assertFalse(hittableA.contains(daniel));

        assertFalse(hittableB.contains(bruce));
        assertFalse(hittableB.contains(charlie));
        assertTrue(hittableB.contains(daniel));
    }

    @Test
    void vortexTest() throws RemoteException{
        weapon = weapon = weaponFactory.createWeapon(WeaponType.VORTEXCANNON);
        ada.grabWeapon(weapon);
        ada.move(board.getSquare(new Coordinate(1, 2)));
        bruce.move(board.getSquare(new Coordinate(1, 2)));
        charlie.move(board.getSquare(new Coordinate(0, 1)));
        daniel.move(board.getSquare(new Coordinate(2, 1)));

        ada.addAmmo(AmmoColor.RED);

        // Inject Selection
        adaView.addSelectable(weapon.getAttacks().get(1).getUuid());
        Square vortexSquare = board.getSquare(new Coordinate(1, 1));
        adaView.addSelectable(vortexSquare.getUuid());
        adaView.addSelectable(Arrays.asList(bruce.getUuid(), charlie.getUuid(), daniel.getUuid()));

        controller.shoot(ada, weapon);

        assertEquals(bruce.getSquare(), vortexSquare);
        assertEquals(charlie.getSquare(), vortexSquare);
        assertEquals(daniel.getSquare(), vortexSquare);
        assertEquals(bruce.getDamageTaken().size(), 1);
        assertEquals(charlie.getDamageTaken().size(), 1);
        assertEquals(daniel.getDamageTaken().size(), 1);
    }

    @Test
    void flamethrowerTest() throws RemoteException{
        weapon = weapon = weaponFactory.createWeapon(WeaponType.FLAMETHROWER);
        ada.grabWeapon(weapon);
        ada.move(board.getSquare(new Coordinate(2, 0)));
        bruce.move(board.getSquare(new Coordinate(1, 0)));
        charlie.move(board.getSquare(new Coordinate(0, 0)));
        daniel.move(board.getSquare(new Coordinate(2, 1)));

        ada.addAmmo(AmmoColor.YELLOW);
        ada.addAmmo(AmmoColor.YELLOW);

        // Inject choices
        adaView.addSelectable(weapon.getAttacks().get(1).getUuid());
        adaView.addSelectable(bruce.getSquare().getUuid());
        adaView.addSelectable(charlie.getSquare().getUuid());

        controller.shoot(ada, weapon);

        assertEquals(bruce.getDamageTaken().size(), 2);
        assertEquals(charlie.getDamageTaken().size(), 1);
    }

    @Test
    void powergloveTest() throws RemoteException{
        weapon = weapon = weaponFactory.createWeapon(WeaponType.POWERGLOVE);
        ada.grabWeapon(weapon);
        ada.move(board.getSquare(new Coordinate(2, 1)));
        bruce.move(board.getSquare(new Coordinate(2, 2)));
        charlie.move(board.getSquare(new Coordinate(2, 2)));
        daniel.move(board.getSquare(new Coordinate(2, 3)));

        ada.addAmmo(AmmoColor.BLUE);
        Attack selectedAttack = weapon.getAttacks().get(1);
        Square selectSquareA = board.getSquare(new Coordinate(2, 2));
        Square selectSquareB = board.getSquare(new Coordinate(2, 3));

        // Inject choices
        adaView.addSelectable(selectedAttack.getUuid());
        adaView.addSelectable(selectSquareA.getUuid());
        adaView.addSelectable(selectSquareB.getUuid());
        adaView.addSelectable(bruce.getUuid());
        adaView.addSelectable(daniel.getUuid());

        controller.shoot(ada, weapon);

        assertEquals(ada.getSquare(), board.getSquare(new Coordinate(2, 3)));
        assertEquals(bruce.getDamageTaken().size(), 2);
        assertEquals(charlie.getDamageTaken().size(), 0);
        assertEquals(daniel.getDamageTaken().size(), 2);
        assertEquals(bruce.getSquare(), board.getSquare(new Coordinate(2, 2)));
        assertEquals(charlie.getSquare(), board.getSquare(new Coordinate(2, 2)));
        assertEquals(daniel.getSquare(), board.getSquare(new Coordinate(2, 3)));
    }

    @Test
    void thorTest() throws RemoteException{
        weapon = weapon = weaponFactory.createWeapon(WeaponType.THOR);
        ada.grabWeapon(weapon);
        ada.move(board.getSquare(new Coordinate(1, 3)));
        bruce.move(board.getSquare(new Coordinate(1, 1)));
        charlie.move(board.getSquare(new Coordinate(0, 1)));
        daniel.move(board.getSquare(new Coordinate(1, 0)));

        ada.addAmmo(AmmoColor.BLUE);
        ada.addAmmo(AmmoColor.BLUE);

        Attack firstAdditional = weapon.getAttacks().get(0).getAdditionalAttacks().get(0);
        Attack secondAdditional = firstAdditional.getAdditionalAttacks().get(0);

        // Inject choices
        adaView.addSelectable(bruce.getUuid());
        adaView.addSelectable(firstAdditional.getUuid());
        adaView.addSelectable(charlie.getUuid());
        adaView.addSelectable(secondAdditional.getUuid());
        adaView.addSelectable(daniel.getUuid());

        controller.shoot(ada, weapon);

        assertEquals(bruce.getDamageTaken().size(), 2);
        assertEquals(charlie.getDamageTaken().size(), 1);
        assertEquals(daniel.getDamageTaken().size(), 2);
    }
}