package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.InvalidOperationException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PowerUp;

import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentGateway {

    private PaymentGateway(){
        super();
    }

    /**
     * Pay a certain cost, you MUST verify that the player can pay che cost using the canPay method, otherwise this won't
     * work. For now, if you are able to pay with powerups, the last powerup will be selected.
     * @param cost The cost to be payed
     */
    public static void payCost(List<AmmoColor> cost, Player player, RemotePlayer remotePlayer) throws RemoteException {
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
                PowerUp selectedPwu = remotePlayer.selectIdentifiable(payablePwu, 1, 1).get(0);
                player.removePowerUp(selectedPwu);
            }
        }
    }
}
