package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.network.TestView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TurnControllerTest {
    private Match match;
    private TurnController turnController;
    private Map<String, RemotePlayer> remoteUsers;
    private SpawnPoint blueSpawn;
    private Random rand;
    private TestView alfView;
    private Player player;

    @BeforeEach
    void setUp(){
        rand = new Random();
        List<String> nicks = new ArrayList<>();
        nicks.add("Alfonsina");
        nicks.add("Bergolio");
        nicks.add("Cristian");
        nicks.add("Dante");

        remoteUsers = new HashMap<>();
        for(String nick : nicks)
            remoteUsers.put(nick, new RemotePlayer(nick, new TestView()));
        alfView = (TestView) remoteUsers.get("Alfonsina").getView();
        match = new Match(nicks, BoardType.SMALL);
        turnController = new TurnController(match, remoteUsers);

        player = match.getPlayerByNickname("Alfonsina");
        blueSpawn = match.getGameBoard().getBoard().getSpawnPointByColor(AmmoColor.BLUE);

    }

    @Test
    void turnMovMov() throws RemoteException {
        player.move(blueSpawn);

        // First we have to choose the action
        Action action = player.supplyActions(match.getFinalFrenzy()).stream()
                .filter(e -> e.canOnlyMove() && e.getActions().size() == 3 )
                .collect(Collectors.toList()).get(0);
        alfView.toBeSelected.add(action.getUuid());

        // The choices should be only two: first movement + second movement (distance 3 each)
        // Impose first movement
        List<Square> firstMovList = blueSpawn.getSquaresByDistance(3);
        Square firstMov = firstMovList.get(rand.nextInt(firstMovList.size()));
        alfView.toBeSelected.add(firstMov.getUuid());
        // Choose second action
        alfView.toBeSelected.add(action.getUuid());

        // Impose second movement
        List<Square> secondMovList = firstMov.getSquaresByDistance(3);
        Square secondMov = secondMovList.get(rand.nextInt(secondMovList.size()));
        alfView.toBeSelected.add(secondMov.getUuid());
        turnController.startTurn();

        // Let's see if everything has gone right
        assertEquals(player.getSquare(), secondMov);
        assertTrue(secondMov.getPlayers().contains(player));
        for(Square square : match.getGameBoard().getBoard().getAllSquares()){
            if(!square.equals(secondMov))
                assertFalse(square.getPlayers().contains(player));
        }
    }

    @Test
    void turnMovGrab() throws RemoteException{
        player.move(blueSpawn);

        // First action: movement
        Action actionMov = player.supplyActions(match.getFinalFrenzy()).stream()
                .filter(e -> e.canOnlyMove() && e.getActions().size() == 3)
                .collect(Collectors.toList()).get(0);
        alfView.toBeSelected.add(actionMov.getUuid());

        // Where shall we move?
        List<Square> movList = blueSpawn.getSquaresByDistance(3);
        Square firstMov = movList.get(rand.nextInt(movList.size()));
        alfView.toBeSelected.add(firstMov.getUuid());

        // Second action: grab
        Action actionGrab = player.supplyActions(match.getFinalFrenzy()).stream()
                .filter(e -> e.canMove() && e.canGrab())
                .collect(Collectors.toList()).get(0);
        alfView.toBeSelected.add(actionGrab.getUuid());

        // Another movement, must end on a standard square
        movList = firstMov.getSquaresByDistance(1).stream()
                .filter(e -> match.getGameBoard().getBoard().isStandardSquare(e.getCoordinates()))
                .collect(Collectors.toList());
        StandardSquare secondMov = (StandardSquare) movList.get(rand.nextInt(movList.size()));
        alfView.toBeSelected.add(secondMov.getUuid());

        AmmoTile tile = secondMov.getAmmoTile();

        turnController.startTurn();

        // If there was a powerup, we should have one too
        assertEquals(tile.hasPowerUp(), !player.getPowerUps().isEmpty());
        // Checking ammo
        for(AmmoColor color : AmmoColor.values()){
            assertEquals(player.countAmmo(color), tile.countAmmo(color));
        }
        assertEquals(player.getSquare(), secondMov);
    }

    @Test
    void turnGrabGrabReload() throws RemoteException{
        player.move(blueSpawn);
        // The only action of this turn, will be repeated
        Action actionGrab = player.supplyActions(match.getFinalFrenzy()).stream()
                .filter(e -> e.canMove() && e.canGrab())
                .collect(Collectors.toList()).get(0);
        alfView.toBeSelected.add(actionGrab.getUuid());

        // Where do we go?
        List<Square> movList = blueSpawn.getSquaresByDistance(1).stream()
                .filter(e -> match.getGameBoard().getBoard().isStandardSquare(e.getCoordinates()))
                .collect(Collectors.toList());
        StandardSquare firstMov = (StandardSquare) movList.get(rand.nextInt(movList.size()));
        alfView.toBeSelected.add(firstMov.getUuid());

        // Inject tile
        AmmoTile firstTile = new AmmoTile(AmmoColor.RED, AmmoColor.RED, AmmoColor.YELLOW);
        firstMov.removeAmmoTile();
        firstMov.setAmmoTile(firstTile);

        // Same action
        alfView.toBeSelected.add(actionGrab.getUuid());

        // Second movement
        movList = firstMov.getSquaresByDistance(1).stream()
                .filter(e -> !e.equals(firstMov) && match.getGameBoard().getBoard().isStandardSquare(e.getCoordinates()))
                .collect(Collectors.toList());
        StandardSquare secondMov = (StandardSquare) movList.get(rand.nextInt(movList.size()));
        alfView.toBeSelected.add(secondMov.getUuid());

        // Inject second tile
        AmmoTile secondTile = new AmmoTile(AmmoColor.YELLOW, AmmoColor.BLUE, AmmoColor.RED);
        secondMov.removeAmmoTile();
        secondMov.setAmmoTile(secondTile);

        // Give the player some weapons to recharge
        Weapon weaponRR = new Weapon("RedRed");
        weaponRR.setAdditionalRechargeColor(AmmoColor.RED);
        weaponRR.addPickupColor(AmmoColor.RED);
        weaponRR.setCharged(false);
        Weapon weaponYB = new Weapon("YellowBlue");
        weaponYB.setAdditionalRechargeColor(AmmoColor.YELLOW);
        weaponYB.addPickupColor(AmmoColor.BLUE);
        weaponYB.setCharged(false);
        Weapon weaponBB = new Weapon("BlueBlue");
        weaponBB.setAdditionalRechargeColor(AmmoColor.BLUE);
        weaponBB.addPickupColor(AmmoColor.BLUE);
        weaponBB.setCharged(false);

        player.grabWeapon(weaponRR);
        player.grabWeapon(weaponYB);
        player.grabWeapon(weaponBB);

        turnController.startTurn();

        assertTrue(secondMov.getPlayers().contains(player));
        assertFalse(weaponBB.getCharged());
        assertTrue(weaponRR.getCharged());
        assertTrue(weaponYB.getCharged());
        assertEquals(player.countAmmo(AmmoColor.RED), 1);
        assertEquals(player.countAmmo(AmmoColor.BLUE), 0);
        assertEquals(player.countAmmo(AmmoColor.YELLOW), 1);
    }
}