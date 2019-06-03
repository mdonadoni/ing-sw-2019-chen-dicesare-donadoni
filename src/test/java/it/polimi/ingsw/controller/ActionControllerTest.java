package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.network.TestView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ActionControllerTest {

    private Match match;
    private Map<String, RemotePlayer> users;
    private ActionController controller;
    private Square redSpawn;
    private TestView adaView;
    private Player player;
    private Action action;
    private StandardSquare shouldEndHere;
    private SpawnPoint goHereSpawn;
    private List<Weapon> weapons;
    private Weapon weaponOne;
    private Weapon weaponTwo;
    private Weapon weaponThree;

    @BeforeEach
    void setUp(){
        List<String> nicks = new ArrayList<String>(Arrays.asList("Ada", "Bruce", "Charlie", "Daniel"));
        match = new Match(nicks, BoardType.SMALL);
        users = new HashMap<>();
        for(String nick : nicks){
            users.put(nick, new RemotePlayer(nick, new TestView()));
        }
        adaView = (TestView) users.get("Ada").getView();
        controller = new ActionController(match, users);
        redSpawn = match.getGameBoard().getBoard().getSpawnPointByColor(AmmoColor.RED);
        match.getGameBoard().refillAmmoTile();
        player = match.getPlayerByNickname("Ada");
        action = new Action();
        shouldEndHere = new StandardSquare(new Coordinate(0,0));
        goHereSpawn = new SpawnPoint(new Coordinate(0,0), AmmoColor.RED);
        users.get("Ada").setTimeLeft(2000);

        weapons = new ArrayList<>();
        weaponOne = new Weapon("One");
        weaponTwo = new Weapon("Two");
        weaponThree = new Weapon("Three");
        weapons.add(weaponOne);
        weapons.add(weaponTwo);
        weapons.add(weaponThree);
    }

    @Test
    void performActionMovement() throws RemoteException {
        player.move(redSpawn);
        assertEquals(redSpawn, player.getSquare());

        // Testing a standard movement action
        action.addAction(BasicAction.MOVEMENT);
        action.addAction(BasicAction.MOVEMENT);

        // Looking for a suitable square to move on

        for(Square sq : redSpawn.getSquaresByDistance(2)){
            if(sq instanceof StandardSquare){
                adaView.toBeSelected = sq.getUuid();
                shouldEndHere = (StandardSquare) sq;
            }
        }
        // The player now should move to the square selected before
        controller.performAction(player, action);
        assertTrue(shouldEndHere.getPlayers().contains(player));
        assertEquals(shouldEndHere, player.getSquare());
        assertFalse(redSpawn.getPlayers().contains(player));
    }

    @Test
    void performActionGrabAmmo() throws RemoteException{
        // Building the action
        action.addAction(BasicAction.MOVEMENT);
        action.addAction(BasicAction.GRAB);

        player.move(redSpawn); // Go to the redSpawn
        assertEquals(redSpawn, player.getSquare());
        assertTrue(redSpawn.getPlayers().contains(player));
        // Check the action
        assertEquals(action.getActions().get(0), BasicAction.MOVEMENT);
        assertEquals(action.getActions().get(1), BasicAction.GRAB);
        assertEquals(action.getActions().size(), 2);

        // Select a new square to move on, now distance is only 1
        for(Square sq : redSpawn.getSquaresByDistance(1)){
            if(sq instanceof StandardSquare){
                adaView.toBeSelected = sq.getUuid();
                shouldEndHere = (StandardSquare) sq;
            }
        }
        assertNotNull(shouldEndHere.getAmmoTile()); // Let's hope there's something on that square
        AmmoTile tile = shouldEndHere.getAmmoTile();
        controller.performAction(player, action);
        // Check everything gone right
        for (AmmoColor color : AmmoColor.values()) {
            assertEquals(tile.countAmmo(color), player.countAmmo(color));
        }
    }

    @Test
    void performActionGrabWeapon() throws RemoteException{
        // Building the action
        action.addAction(BasicAction.MOVEMENT);
        action.addAction(BasicAction.MOVEMENT);
        action.addAction(BasicAction.MOVEMENT);
        action.addAction(BasicAction.GRAB);
        assertEquals(action.getActions().size(), 4);

        // Set the player to the redSpawn
        player.move(redSpawn);
        assertTrue(redSpawn.getPlayers().contains(player));
        assertEquals(player.getSquare(), redSpawn);

        // Select a SpawnSquare different to the redSpawn
        for(Square sq : redSpawn.getSquaresByDistance(3)){
            if(!sq.equals(redSpawn) && sq instanceof SpawnPoint){
                adaView.toBeSelected = sq.getUuid();
                goHereSpawn = (SpawnPoint) sq;
            }
        }
        // Place some weapons on the spawnPoint selected for the movement
        goHereSpawn.addWeapon(weaponOne);
        goHereSpawn.addWeapon(weaponTwo);
        goHereSpawn.addWeapon(weaponThree);
        // Notice that I can tell the view to move to the goHereSpawn square, but I cannot command also the
        // weapon, btw that's not a problem

        controller.performAction(player, action);

        // Checking everything went right
        assertEquals(goHereSpawn.getWeapons().size(), 2);
        for(Weapon wp : weapons){
            if(wp.equals(player.getWeapons().get(0)))
                assertFalse(goHereSpawn.getWeapons().contains(wp));
            else
                assertTrue(goHereSpawn.getWeapons().contains(wp));
        }
    }

    @Test
    void performActionGrabWeaponDiscard() throws RemoteException{
        player.grabWeapon(weaponOne);
        player.grabWeapon(weaponTwo);
        player.grabWeapon(weaponThree);

        Weapon extraWeapon = new Weapon("EXXXTRA");

        action.addAction(BasicAction.GRAB);
        player.move(redSpawn);
        goHereSpawn = (SpawnPoint) redSpawn;
        goHereSpawn.addWeapon(extraWeapon);

        controller.performAction(player, action);

        assertTrue(player.getWeapons().contains(extraWeapon));
        assertEquals(goHereSpawn.getWeapons().size(), 1);
    }
}