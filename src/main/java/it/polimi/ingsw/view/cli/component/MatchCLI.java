package it.polimi.ingsw.view.cli.component;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.minified.MiniMatch;
import it.polimi.ingsw.model.minified.MiniPlayer;
import it.polimi.ingsw.view.cli.util.CharCli;

import java.util.ArrayList;
import java.util.List;

public class MatchCLI {
    private GameBoardCLI gameBoardCLI;
    private ArrayList<PlayerCLI> playerCLI;
    private MiniMatch miniMatch;

    public MatchCLI(MiniMatch miniMatch) {
        this.miniMatch = miniMatch;
        this.gameBoardCLI = new GameBoardCLI(miniMatch.getGameBoard());
        this.playerCLI = new ArrayList<>();
        for(MiniPlayer p : miniMatch.getPlayers()){
            playerCLI.add(new PlayerCLI(p,null));
        }
    }

    public List viewMatch(){
        ArrayList<String> outList = new ArrayList<>();
        ArrayList<String> squareList = new ArrayList<>();
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

        CharCli.concatRow( outList ,squareList);
        return outList;
    }


}
