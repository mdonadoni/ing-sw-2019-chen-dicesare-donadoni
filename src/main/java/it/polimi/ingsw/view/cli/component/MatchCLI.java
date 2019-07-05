package it.polimi.ingsw.view.cli.component;

import it.polimi.ingsw.model.minified.MiniMatch;
import it.polimi.ingsw.model.minified.MiniPlayer;
import it.polimi.ingsw.view.cli.util.CharCli;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent the Match in the CLI
 */
public class MatchCLI {
    /**
     * The game board CLI
     */
    private GameBoardCLI gameBoardCLI;
    /**
     * List of players
     */
    private ArrayList<PlayerCLI> playerCLI;
    /**
     * Match to represent
     */
    private MiniMatch miniMatch;

    /**
     * Constructor
     * @param miniMatch match to represent
     */
    public MatchCLI(MiniMatch miniMatch) {
        this.miniMatch = miniMatch;
        this.gameBoardCLI = new GameBoardCLI(miniMatch.getGameBoard());
        this.playerCLI = new ArrayList<>();
        for(MiniPlayer p : miniMatch.getPlayers()){
            playerCLI.add(new PlayerCLI(p,null));
        }
    }

    /**
     * Generate the list of strings that represent the match
     * @return list of strings that represent the match
     */
    public List<String> viewMatch(){
        List<String> outList = new ArrayList<>();
        List<String> squareList = new ArrayList<>();
        String out;
        //current turn
        out = "Turno di : " +miniMatch.getCurrentTurn().getCurrentPlayer();
        squareList.add(out);
        //players
        outList.add("");
        outList.add("");
        for( PlayerCLI p : playerCLI){
            outList.addAll(p.viewPlayer());
        }
        //gameboard
        squareList.add("");
        squareList.addAll(gameBoardCLI.viewGameBoard());

        while(outList.size() < squareList.size()) {
            outList.add(CharCli.addSpace("", PlayerCLI.LENGTH+2));
        }

        CharCli.concatRow( outList ,squareList);
        return outList;
    }


}
