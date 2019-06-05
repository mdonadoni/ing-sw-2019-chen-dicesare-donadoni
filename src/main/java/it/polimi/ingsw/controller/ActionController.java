package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.view.dialogs.DialogType;
import it.polimi.ingsw.view.dialogs.UserDialog;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller class that manages an Action
 */
public class ActionController {

    private Match match;
    private Map<String, RemotePlayer> remoteUsers;
    private static final int DIALOGTIMEOUT = 3000;

    /**
     * Standard constructor
     * @param match the match currently going on
     * @param remoteUsers a map containing all the RemotePlayer references
     */
    public ActionController(Match match, Map<String, RemotePlayer> remoteUsers){
        this.match = match;
        this.remoteUsers = remoteUsers;
    }

    /**
     * Does everything needed to fully perform an action
     * @param player the player acting
     * @param action the complete action
     * @throws RemoteException in case something goes wrong
     */
    public void performAction(Player player, Action action) throws RemoteException{
        // Now cycle through all the basic actions, collecting together the movement actions
        Iterator<BasicAction> iter = action.getActions().iterator();
        BasicAction basic;
        String playerName = player.getNickname();
        int nMov = 0;
        while(iter.hasNext()){
            basic = iter.next();
            if(basic == BasicAction.MOVEMENT){ // Count how many basic movements I have, so that I can do them all at once
                nMov++;
            }
            else{
                if(nMov>0){ // At the first non-movement should handle the collected movements
                    handleMovement(playerName, nMov);
                }
                // Then handle the current basic action
                handleBasicAction(playerName, basic);
                nMov = 0; // In any case I want to reset the nMov counter
            }
        }
        if(nMov>0)
            handleMovement(playerName, nMov);
        nMov = 0;
    }

    /**
     * Handles a single basic action
     * @param playerName the player acting
     * @param basic the basic action type
     * @throws RemoteException in case something goes wrong
     */
    private void handleBasicAction(String playerName, BasicAction basic) throws RemoteException{
        switch(basic){
            case MOVEMENT:
                handleMovement(playerName, 1);
                break;
            case RELOAD:
                handleReload(playerName);
                break;
            case GRAB:
                handleGrab(playerName);
                break;
            case SHOOT:
                handleShoot(playerName);
                break;
            case SKIP:
                handleSkip(playerName);
                break;
        }

    }

    /**
     * Manages a Movement Action, moving around the player
     * @param playerName the player acting
     * @param nMov how far the player can go
     * @throws RemoteException in case something goes wrong
     */
    private void handleMovement(String playerName, int nMov) throws RemoteException{
        Player player = match.getPlayerByNickname(playerName);
        RemotePlayer remotePlayer = remoteUsers.get(playerName);
        Square selectedSquare;
        // Build the list of the squares available for the movement
        List<Square> whereToMove = player.getSquare().getSquaresByDistance(nMov);
        // Ask the player where to move
        selectedSquare = remotePlayer.selectIdentifiable(whereToMove, 1, 1).get(0);
        // Apply the movement
        player.move(selectedSquare);

    }

    /**
     * Handled the recharge routine for a player
     * @param playerName the player acting
     * @throws RemoteException in case something goes wrong
     */
    private void handleReload(String playerName) throws RemoteException {
        Player player = match.getPlayerByNickname(playerName);
        RemotePlayer remotePlayer = remoteUsers.get(playerName);
        // Cycle through all the player weapons
        for(Weapon currentWeapon : player.getWeapons()){
            if(player.canPay(currentWeapon.getTotalRechargeCost())){
                // Ask the user if he wants to recharge this weapon
                List<Weapon> selectingWeapon = new ArrayList<>();
                selectingWeapon.add(currentWeapon);
                remotePlayer.selectIdentifiable(selectingWeapon, 0, 1);
                // Pay the recharge cost
                PaymentGateway.payCost(currentWeapon.getTotalRechargeCost(), player, remotePlayer);
                // Set the recharge flag
                currentWeapon.setCharged(true);
            }
        }
    }

    /**
     * The player performs the grab action, if he's on a StandardSquare, automatically gets the ammotile, otherwise
     * asks the user which weapon he wants to grab and handles possible difficult situations
     * @param playerName the player who's acting
     * @throws RemoteException in case something goes wrong
     */
    private void handleGrab(String playerName)  throws RemoteException{
        Player player = match.getPlayerByNickname(playerName);
        RemotePlayer remotePlayer = remoteUsers.get(playerName);
        Board board = match.getGameBoard().getBoard();

        // Check if the player is on a standard square
        if(board.isStandardSquare(player.getSquare().getCoordinates())){
            StandardSquare sq = (StandardSquare) player.getSquare();
            // Automatically grab the ammoTile, if there's one
            if(sq.hasAmmoTile()){
                player.grabAmmo(sq.getAmmoTile());
                sq.removeAmmoTile();
            }
        }
        else{ // If the player is on a SpawnPoint, I must ask him what weapon he wants to grab
            SpawnPoint spw = (SpawnPoint) player.getSquare();
            List<Weapon> weapons = spw.getWeapons();
            // Filter the weapons so that I prompt the user only those who can be grabbed
            weapons = weapons.stream().filter(e -> player.canPay(e.getPickupColor())).collect(Collectors.toList());
            Weapon selectedWeapon = remotePlayer.selectIdentifiable(weapons, 1, 1).get(0);
            // Grab the weapon
            try{
                player.grabWeaponFromGround(selectedWeapon);
                PaymentGateway.payCost(selectedWeapon.getPickupColor(), player, remotePlayer); // Pay the cost
            } catch (FullInventoryException e){ // If we already have 3 weapons, we must discard one
                discardWeapon(selectedWeapon, player);
            }
        }
    }

    /**
     * Dummy method, will be implemented later
     */
    private void handleShoot(String playerName) throws RemoteException{
        Player player = match.getPlayerByNickname(playerName);
        RemotePlayer remotePlayer = remoteUsers.get(playerName);
        List<Player> otherPlayers = match.getOtherPlayers(playerName);

        Player victim = remotePlayer.selectIdentifiable(otherPlayers, 1, 1).get(0);

        victim.takeDamage(player.getColor(), 2);
        victim.addMark(player.getColor(), 1);
    }

    /**
     * Just notifies the user that he skipped his turn
     * @param playerName the player I want to notify
     * @throws RemoteException in case something bad happens
     */
    private void handleSkip(String playerName) throws RemoteException {
        RemotePlayer remotePlayer = remoteUsers.get(playerName);
        remotePlayer.showMessage(UserDialog.getDialog(DialogType.SKIP_DIALOG), DIALOGTIMEOUT);
    }

    /**
     * Makes the player discard a weapon and then adds the weapon that didn't fit before
     * @param extraOne the weapon he couldn't grab
     * @param player the player who's acting
     * @throws RemoteException in case something goes wrong
     */
    private void discardWeapon(Weapon extraOne, Player player) throws RemoteException{
        List<Weapon> weapons = new ArrayList<>(player.getWeapons());
        RemotePlayer remote = remoteUsers.get(player.getNickname());
        SpawnPoint spw = (SpawnPoint) player.getSquare();

        Weapon selectedWeapon = remote.selectIdentifiable(weapons, 1, 1).get(0);
        player.removeWeapon(selectedWeapon);
        selectedWeapon.setCharged(true);
        spw.addWeapon(selectedWeapon);
        player.grabWeaponFromGround(extraOne);
    }
}
