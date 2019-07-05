package it.polimi.ingsw.view.cli.component;

import it.polimi.ingsw.model.minified.MiniModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent the model in the CLI
 */
public class ModelCLI {
    /**
     * Model to represent
     */
    private MiniModel miniModel;
    /**
     * Match CLI
     */
    private MatchCLI matchCLI;
    /**
     * Player CLI
     */
    private PlayerCLI playerCLI;

    /**
     * Constructor
     * @param miniModel the model to represent
     */
    public ModelCLI(MiniModel miniModel) {
        this.miniModel = miniModel;
        this.playerCLI = new PlayerCLI(miniModel.getMyMiniPlayer(), miniModel.getMyPowerUps());
        miniModel.getMatch().getPlayers().remove(miniModel.getMyMiniPlayer());
        this.matchCLI = new MatchCLI(miniModel.getMatch());
    }

    /**
     * Generate the list of strings that represent the model
     * @return list of strings that represent the model
     */
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
