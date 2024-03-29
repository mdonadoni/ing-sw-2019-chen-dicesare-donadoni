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
    private SpawnPoint redSpawn;
    private TestView adaView;
    private Player player;
    private Action action;
    private StandardSquare shouldEndHere;
    private SpawnPoint goHereSpawn;
    private List<Weapon> weapons;
    private Weapon weaponOne;
    private Weapon weaponTwo;
    private Weapon weaponThree;
    private PowerUp teleporter;
    private PowerUp newton;
    private PowerUp targeting;

    @BeforeEach
    void setUp(){
        List<String> nicks = new ArrayList<String>(Arrays.asList("Ada", "Bruce", "Charlie", "Daniel"));
        match = new Match(nicks, new JsonModelFactory(BoardType.SMALL));
        users = new HashMap<>();
        for(String nick : nicks){
            users.put(nick, new RemotePlayer(nick, new TestView()));
        }
        adaView = (TestView) users.get("Ada").getView();
        controller = new ActionController(match, users, new Updater(users, match));
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

        teleporter = new PowerUp(PowerUpType.TELEPORTER, AmmoColor.YELLOW);
        newton = new PowerUp(PowerUpType.NEWTON, AmmoColor.BLUE);
        targeting = new PowerUp(PowerUpType.TARGETING_SCOPE, AmmoColor.RED);

        for(SpawnPoint spw : match.getGameBoard().getBoard().getSpawnPoints())
            spw.removeAllWeapons();
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
                adaView.toBeSelected.clear();
                adaView.addSelectable(sq.getUuid());
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
                adaView.toBeSelected.clear();
                adaView.addSelectable(sq.getUuid());
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
                adaView.addSelectable(sq.getUuid());
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
        goHereSpawn = redSpawn;
        goHereSpawn.addWeapon(extraWeapon);

        controller.performAction(player, action);

        assertTrue(player.getWeapons().contains(extraWeapon));
        assertEquals(goHereSpawn.getWeapons().size(), 1);
    }

    @Test
    void performActionGrabWeaponWithCost() throws RemoteException{
        // Setup weapon costs
        weaponOne.addPickupColor(AmmoColor.RED);
        weaponOne.addPickupColor(AmmoColor.BLUE);
        weaponOne.addPickupColor(AmmoColor.BLUE);
        weaponTwo.addPickupColor(AmmoColor.YELLOW);
        weaponThree.addPickupColor(AmmoColor.YELLOW);
        weaponThree.addPickupColor(AmmoColor.BLUE);

        // Add weapons to the spawn
        redSpawn.addWeapon(weaponOne);
        redSpawn.addWeapon(weaponTwo);
        redSpawn.addWeapon(weaponThree);

        // Add player something to pay with
        player.addAmmo(AmmoColor.RED);
        player.addAmmo(AmmoColor.BLUE);
        player.addPowerUp(new PowerUp(PowerUpType.NEWTON, AmmoColor.BLUE));
        player.addPowerUp(new PowerUp(PowerUpType.TELEPORTER, AmmoColor.BLUE));

        // Setup action
        action.addAction(BasicAction.GRAB);
        player.move(redSpawn);

        // Do the damn thing
        controller.performAction(player, action);

        // Please don't crash
        assertEquals(redSpawn.getWeapons().size(), 2);
        assertEquals(player.getWeapons().size(), 1);
        assertTrue(player.getAmmo().isEmpty());
        assertEquals(player.getPowerUps().size(), 1);
    }

    @Test
    void performActionReload() throws RemoteException{
        // Setup action
        action.addAction(BasicAction.RELOAD);

        // Setup weapon costs
        weaponOne.addPickupColor(AmmoColor.RED);
        weaponOne.addPickupColor(AmmoColor.RED);
        weaponOne.setAdditionalRechargeColor(AmmoColor.BLUE);
        weaponTwo.addPickupColor(AmmoColor.RED);
        weaponTwo.setAdditionalRechargeColor(AmmoColor.BLUE);

        // Add weapons to player
        player.grabWeapon(weaponOne);
        player.grabWeapon(weaponTwo);
        player.grabWeapon(weaponThree);

        // Set weapons ad discharged
        for(Weapon wp : player.getWeapons()){
            wp.setCharged(false);
        }

        // Give player something to pay with
        player.addAmmo(AmmoColor.RED);
        player.addAmmo(AmmoColor.RED);
        PowerUp bluePwu = new PowerUp(PowerUpType.NEWTON, AmmoColor.BLUE);
        player.addPowerUp(bluePwu);
        player.addPowerUp(new PowerUp(PowerUpType.TELEPORTER, AmmoColor.YELLOW));

        adaView.addSelectable(weaponOne.getUuid());
        adaView.addSelectable(bluePwu.getUuid());
        adaView.addSelectable(weaponThree.getUuid());

        // Do the thing
        controller.performAction(player, action);

        // Check everything gone right
        assertTrue(player.getAmmo().isEmpty());
        assertEquals(player.getPowerUps().size(), 1);
        assertEquals(player.getPowerUps().get(0).getAmmo(), AmmoColor.YELLOW);
        assertTrue(weaponOne.isCharged());
        assertFalse(weaponTwo.isCharged());
        assertTrue(weaponThree.isCharged());
    }

    @Test
    void performDrawPowerUpNoDiscard() throws RemoteException{
        action.addAction(BasicAction.GRAB);

        StandardSquare stdSq = match.getGameBoard().getBoard().getStandardSquares().get(0);
        player.move(stdSq);

        stdSq.removeAmmoTile();
        stdSq.setAmmoTile(new AmmoTile(AmmoColor.RED, AmmoColor.RED));

        PowerUp peekPwu = match.getGameBoard().getPowerUpDeck().peek();

        player.addPowerUp(teleporter);
        player.addPowerUp(newton);

        controller.performAction(player, action);

        assertTrue(player.getPowerUps().contains(teleporter));
        assertTrue(player.getPowerUps().contains(newton));
        assertTrue(player.getPowerUps().contains(peekPwu));
    }

    @Test
    void performDrawPowerUpWithDiscard() throws RemoteException{
        action.addAction(BasicAction.GRAB);

        StandardSquare stdSq = match.getGameBoard().getBoard().getStandardSquares().get(1);
        player.move(stdSq);

        stdSq.removeAmmoTile();
        stdSq.setAmmoTile(new AmmoTile(AmmoColor.RED, AmmoColor.RED));

        PowerUp peekPwu = match.getGameBoard().getPowerUpDeck().peek();

        player.addPowerUp(teleporter);
        player.addPowerUp(newton);
        player.addPowerUp(targeting);

        adaView.addSelectable(teleporter.getUuid());

        controller.performAction(player, action);

        assertTrue(player.getPowerUps().contains(targeting));
        assertTrue(player.getPowerUps().contains(newton));
        assertTrue(player.getPowerUps().contains(peekPwu));
        assertFalse(player.getPowerUps().contains(teleporter));
        assertTrue(match.getGameBoard().getPowerUpDeck().getDiscarded().contains(teleporter));
    }
}