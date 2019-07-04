package it.polimi.ingsw.view.cli.component;

import it.polimi.ingsw.model.minified.MiniModel;

import java.util.ArrayList;
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

    public List<String> viewModel(){
        List<String> outList = new ArrayList<>();
        String out;
        //match
        outList.addAll(matchCLI.viewMatch());
        //player
        outList.addAll(playerCLI.viewPlayer());
        out="Punti : "+miniModel.getMyPoints();
        outList.add(out);
        return outList;
    }
}
