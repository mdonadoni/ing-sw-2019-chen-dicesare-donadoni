package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.weapons.Attack;
import it.polimi.ingsw.model.weapons.PlayerTarget;
import it.polimi.ingsw.model.weapons.Target;
import it.polimi.ingsw.model.weapons.Weapon;
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

    @BeforeEach
    void setup(){
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
        weapon = new Weapon(Weapon.class.getResourceAsStream("/weapons/lockrifle.json"));
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
        weapon = new Weapon(Weapon.class.getResourceAsStream("/weapons/tractorbeam.json"));
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
        weapon = new Weapon(Weapon.class.getResourceAsStream("/weapons/tractorbeam.json"));
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
        adaView.addSelectable(board.getSquare(new Coordinate(2, 1)).getUuid());

        controller.shoot(ada, weapon);

        assertEquals(daniel.getSquare(), board.getSquare(new Coordinate(2, 1)));
        assertEquals(daniel.getMarks().size(), 0);
        assertEquals(daniel.getDamageTaken().size(), 3);
        assertEquals(daniel.getDamageTaken().get(0), ada.getColor());
        assertFalse(weapon.isCharged());
        assertEquals(ada.getAmmo().size(), 1);
    }

    @Test
    void electroscytheTest() throws RemoteException{
        weapon = new Weapon(Weapon.class.getResourceAsStream("/weapons/electroscythe.json"));
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
        weapon = new Weapon(Weapon.class.getResourceAsStream("/weapons/plasmagun.json"));
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
        weapon = new Weapon(Weapon.class.getResourceAsStream("/weapons/machinegun.json"));
        ada.grabWeapon(weapon);
        ada.move(board.getSquare(new Coordinate(1, 1)));
        bruce.move(board.getSquare(new Coordinate(1, 2)));
        charlie.move(board.getSquare(new Coordinate(1, 0)));
        daniel.move(board.getSquare(new Coordinate(2, 2)));


    }
}