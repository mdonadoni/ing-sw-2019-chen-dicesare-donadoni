package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PowerUpController {
    private Match match;
    private Map<String, RemotePlayer> remoteUsers;
    private Board board;

    public PowerUpController(Match match, Map<String, RemotePlayer> remoteUsers){
        this.match = match;
        this.remoteUsers = remoteUsers;
        this.board = match.getGameBoard().getBoard();
    }

    public void activatePowerUp(PowerUp pwu, String sourcePlayer) throws RemoteException{
        // Didn't find a better way, I'm sorry Lord of Code :(
        activatePowerUp(pwu, sourcePlayer, new Player("dummy", PlayerToken.BLUE));
    }

    public void activatePowerUp(PowerUp pwu, String sourcePlayer, Player targetPlayer) throws RemoteException{
        RemotePlayer sourceRemotePlayer = remoteUsers.get(sourcePlayer);
        PlayerToken damageColor = match.getPlayerByNickname(sourcePlayer).getColor();
        switch(pwu.getType()){
            case TAGBACK_GRANADE:
                tagback(targetPlayer, damageColor);
                break;
            case NEWTON:
                newton(sourceRemotePlayer, targetPlayer);
                break;
            case TELEPORTER:
                teleporter(sourceRemotePlayer);
                break;
            case TARGETING_SCOPE:
                targeting(targetPlayer, damageColor);
                break;
        }
    }

    /**
     * Tagback Granade inflicts one mark to a target
     * @param targetPlayer the victim
     * @param damageColor the color of the mark that will be added
     */
    private void tagback(Player targetPlayer, PlayerToken damageColor){
        targetPlayer.addMark(damageColor, 1);
    }

    /**
     * Newton PowerUp allows a player to move another target player on another square, the movement must be done
     * in a single direction
     * @param sourcePlayer the player using this PowerUp
     * @param targetPlayer the target of this PowerUp
     * @throws RemoteException If something happens to go wrong
     */
    private void newton(RemotePlayer sourcePlayer, Player targetPlayer) throws RemoteException{
        Square playerTargetSquare = targetPlayer.getSquare();
        List<Square> squares = new ArrayList<>(playerTargetSquare.getSquaresByDistanceAligned(2));
        // Ask the player for a target square
        Square chosenSquare = sourcePlayer.selectIdentifiable(squares, 1, 1).get(0);
        // Move the bloody victim
        targetPlayer.move(chosenSquare);
    }

    /**
     * Teleporter PowerUp moves the player to any Square he wants
     * @param player the player using this PowerUp
     * @throws RemoteException If something happens to go wrong
     */
    private void teleporter(RemotePlayer player) throws RemoteException {
        // Time to travel!
        // Build the list containing all the squares (teleporting bruh!)
        List<Square> squares = new ArrayList<>(board.getAllSquares());
        // Send the list to the client and wait for a response
        Square chosenSquare = player.selectIdentifiable(squares, 1, 1).get(0);
        // Move the player
        match.getPlayerByNickname(player.getNickname()).move(chosenSquare);
    }

    /**
     * Targeting Scope deals one extra damage to the target, but does NOT trigger pre-existing marks
     * @param targetPlayer the victim of the attack
     * @param damageColor the damage to be added
     */
    private void targeting(Player targetPlayer, PlayerToken damageColor){
        targetPlayer.addDamage(damageColor, 1);
    }
}
