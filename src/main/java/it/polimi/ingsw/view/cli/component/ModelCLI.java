package it.polimi.ingsw.view.cli.component;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PowerUp;
import it.polimi.ingsw.model.minified.MiniModel;

import it.polimi.ingsw.model.minified.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.weapons.Weapon;

import java.rmi.MarshalledObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelCLI {
    private MiniModel miniModel;
    private MatchCLI matchCLI;
    private PlayerCLI playerCLI;

    public ModelCLI(MiniModel miniModel) {
        this.miniModel = miniModel;
        this.playerCLI = new PlayerCLI(miniModel.getMyMiniPlayer(), miniModel.getMyPowerUps());
        miniModel.getMatch().getPlayers().remove(miniModel.getMyMiniPlayer());
        this.matchCLI = new MatchCLI(miniModel.getMatch());
    }

    public List viewModel(){
        ArrayList<String> outList = new ArrayList<>();
        String out;
        //match
        outList.addAll(matchCLI.viewMatch());
        //player
        outList.addAll(playerCLI.viewPlayer());
        out="Punti : "+miniModel.getMyPoints();
        outList.add(out);
        return outList;
    }
/*
    public static void main (String[] args){
        Match match = new Match(
                Arrays.asList("Sim", "Mar", "Fed", "D", "E"),
                new JsonModelFactory(BoardType.SMALL)
        );
        Player pA = match.getPlayerByNickname("Sim");
        Player pB = match.getPlayerByNickname("Mar");
        Player pC = match.getPlayerByNickname("Fed");
        Player pD = match.getPlayerByNickname("D");
        Player pE = match.getPlayerByNickname("E");
        pA.addMark(PlayerToken.BLUE, 2);
        pA.addDamage(PlayerToken.YELLOW, 5);
        pA.addPowerUp(new PowerUp(PowerUpType.NEWTON, AmmoColor.RED));
        Weapon w1 =new Weapon("Vortex");
        w1.setAdditionalRechargeColor(AmmoColor.RED);
        w1.addPickupColor(AmmoColor.YELLOW);
        w1.addPickupColor(AmmoColor.BLUE);
        pA.grabWeapon(w1);

        MiniModel miniModel = new MiniModel(match,match.getPlayerByNickname("Sim"));

        ModelCLI modelCLI = new ModelCLI(miniModel);

        ArrayList<String> s = (ArrayList<String>) modelCLI.viewModel();
        for( String sa : s){
            System.out.println(sa);
        }
    }
*/

}
