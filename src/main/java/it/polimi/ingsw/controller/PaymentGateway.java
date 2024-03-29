package it.polimi.ingsw.controller;

import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.model.*;

import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller that manages all the payments in ammo
 */
public class PaymentGateway {

    /**
     * The match going on
     */
    private Match match;

    /**
     * Constructor
     * @param match The match going on
     */
    public PaymentGateway(Match match){
        this.match = match;
    }

    /**
     * Pay a certain cost, you MUST verify that the player can pay che cost using the canPay method, otherwise this won't
     * work. Firstly pays with the available ammo, then uses the powerups, if needed
     * @param cost The cost to be payed
     * @param player The player that has to pay
     * @param remotePlayer The RemotePlayer object that refers to the player
     * @throws RemoteException thrown when the player can't pay the cost
     */
    public void payCost(List<AmmoColor> cost, Player player, RemotePlayer remotePlayer) throws RemoteException {
        String nickname = player.getNickname();
        if(!player.canPay(cost))
            throw new InvalidOperationException(nickname + " cannot pay the cost");
        // First pay using the ammo of the player
        for(AmmoColor singleCost : cost){
            if(player.getAmmo().contains(singleCost))
                player.removeAmmo(singleCost, 1);
                // If no ammo is available, then use a powerup
            else{
                // Must ask the player which powerup he wants to use
                List<PowerUp> payablePwu = player.getPowerUps().stream()
                        .filter(e -> e.getAmmo().equals(singleCost))
                        .collect(Collectors.toList());
                PowerUp selectedPwu = remotePlayer.selectIdentifiable(payablePwu, 1, 1, Dialog.PAY_COST_POWERUP).get(0);
                player.removePowerUp(selectedPwu);
                match.getGameBoard().getPowerUpDeck().discard(selectedPwu);
            }
        }
    }
}
