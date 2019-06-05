package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.TestView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentGatewayTest {

    private Player player;
    private RemotePlayer remotePlayer;

    @BeforeEach
    void setup(){
        player = new Player("Ada", PlayerToken.BLUE);
        remotePlayer = new RemotePlayer("Ada", new TestView());
        remotePlayer.setTimeLeft(2000);
    }

    @Test
    void payCost() throws RemoteException {
        List<AmmoColor> cost = new ArrayList<>();
        cost.add(AmmoColor.RED);
        cost.add(AmmoColor.BLUE);
        cost.add(AmmoColor.RED);

        player.addAmmo(AmmoColor.YELLOW);
        player.addAmmo(AmmoColor.RED);
        player.addAmmo(AmmoColor.RED);
        player.addAmmo(AmmoColor.BLUE);

        PaymentGateway.payCost(cost, player, remotePlayer);
        // YRRB - RBR => Y
        assertEquals(player.getAmmo().get(0), AmmoColor.YELLOW);

        player.addAmmo(AmmoColor.RED);
        player.addAmmo(AmmoColor.RED);
        player.addPowerUp(new PowerUp(PowerUpType.NEWTON, AmmoColor.BLUE));
        player.addPowerUp(new PowerUp(PowerUpType.NEWTON, AmmoColor.RED));

        PaymentGateway.payCost(cost, player, remotePlayer);
        // YRR[B][R] - RBR => Y[R]
        assertEquals(player.getAmmo().get(0), AmmoColor.YELLOW);
        assertEquals(player.getPowerUps().get(0).getAmmo(), AmmoColor.RED);

        player.addAmmo(AmmoColor.YELLOW);
        player.addPowerUp(new PowerUp(PowerUpType.NEWTON, AmmoColor.BLUE));
        player.addPowerUp(new PowerUp(PowerUpType.NEWTON, AmmoColor.RED));

        PaymentGateway.payCost(cost, player, remotePlayer);
        // YY[R][R][B] - RBR => YY
        assertTrue(player.getPowerUps().isEmpty());
        assertEquals(player.getAmmo().size(), 2);
        assertEquals(player.getAmmo().get(1), AmmoColor.YELLOW);
    }
}