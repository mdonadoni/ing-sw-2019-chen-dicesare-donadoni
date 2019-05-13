package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.PlayerToken;

import java.io.Serializable;
import java.util.ArrayList;

public class MiniGameBoard implements Serializable {
    private static final long serialVersionUID = -2599886836431833259L;

    private final int initialSkullNumber;
    private final int remainingSkulls;
    private final ArrayList<ArrayList<PlayerToken>> killShotTrack;
    private final MiniBoard board;

    @JsonCreator
    private MiniGameBoard() {
        this.initialSkullNumber = 0;
        this.killShotTrack = null;
        this.remainingSkulls = 0;
        this.board = null;
    }

    MiniGameBoard(GameBoard gameBoard) {
        this.initialSkullNumber = gameBoard.getInitialSkullNumber();
        this.remainingSkulls = gameBoard.getRemainingSkulls();
        this.killShotTrack = new ArrayList<>();
        gameBoard.getKillShotTrack().forEach((l) -> killShotTrack.add(new ArrayList<>(l)));
        this.board = new MiniBoard(gameBoard.getBoard());
    }

    public int getInitialSkullNumber() {
        return initialSkullNumber;
    }

    public int getRemainingSkulls() {
        return remainingSkulls;
    }

    public ArrayList<ArrayList<PlayerToken>> getKillShotTrack() {
        return killShotTrack;
    }

    public MiniBoard getBoard() {
        return board;
    }
}
