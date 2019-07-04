package it.polimi.ingsw.view.cli.component;


import it.polimi.ingsw.model.PlayerToken;
import it.polimi.ingsw.model.minified.MiniGameBoard;
import it.polimi.ingsw.view.cli.util.CharCli;
import it.polimi.ingsw.view.cli.util.ColorCLI;

import java.util.ArrayList;
import java.util.List;

public class GameBoardCLI {

    private BoardCLI boardCLI;
    private MiniGameBoard miniGameBoard;

    public GameBoardCLI(MiniGameBoard miniGameBoard){
        this.miniGameBoard = miniGameBoard;
        this.boardCLI = new BoardCLI(miniGameBoard.getBoard());
    }

    public List<String> viewGameBoard(){
        ArrayList<String> outLIst = new ArrayList<>();
        String out=" ";
        PlayerToken pt;
        //player kill shot track
        List<ArrayList<PlayerToken>> killShotTrack = miniGameBoard.getKillShotTrack();
        for(ArrayList<PlayerToken> kills : killShotTrack){
            if(miniGameBoard.getRemainingSkulls()>0){
                pt = kills.get(0);
                if(kills.size()>1) {
                    out = out.concat(" +"+ ColorCLI.getPlayerColor(pt, CharCli.DAMAGE_TOKEN));
                }else {
                    out = out.concat(" "+ColorCLI.getPlayerColor(pt, CharCli.DAMAGE_TOKEN));
                }
            }else{
                out = out.concat("|");
                for(PlayerToken p : kills){
                    out = out.concat(ColorCLI.getPlayerColor(p, CharCli.DAMAGE_TOKEN));
                }
            }
        }
        //remain skulls
        for (int i=miniGameBoard.getRemainingSkulls(); i>0 ; i--){
            out = out.concat(" "+ColorCLI.turnRed(CharCli.SKULL));
        }
        outLIst.add(out);
        //board
        outLIst.addAll(boardCLI.viewBoard());
        return outLIst;
    }
}
