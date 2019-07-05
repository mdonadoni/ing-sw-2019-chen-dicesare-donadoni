package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.PlayerToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * GameBoard for the client side
 */
public class MiniGameBoard implements Serializable {
    /**
     * Serializable UID
     */
    private static final long serialVersionUID = -2599886836431833259L;
    /**
     * Initial number of skull
     */
    private final int initialSkullNumber;
    /**
     * The remaining amount of skulls
     */
    private final int remainingSkulls;
    /**
     * The kill shot track
     */
    private final ArrayList<ArrayList<PlayerToken>> killShotTrack;
    /**
     * The board with spawn points and std squares
     */
    private final MiniBoard board;

    @JsonCreator
    private MiniGameBoard() {
        this.initialSkullNumber = 0;
        this.killShotTrack = null;
        this.remainingSkulls = 0;
        this.board = null;
    }

    /**
     * Constructor of the class
     * @param gameBoard The GameBoard to convert
     */
    MiniGameBoard(GameBoard gameBoard) {
        this.initialSkullNumber = gameBoard.getInitialSkullNumber();
        this.remainingSkulls = gameBoard.getRemainingSkulls();
        this.killShotTrack = new ArrayList<>();
        gameBoard.getKillShotTrack().forEach(l -> killShotTrack.add(new ArrayList<>(l)));
        this.board = new MiniBoard(gameBoard.getBoard());
    }

    public int getInitialSkullNumber() {
        return initialSkullNumber;
    }

    public int getRemainingSkulls() {
        return remainingSkulls;
    }

    public List<ArrayList<PlayerToken>> getKillShotTrack() {
        return killShotTrack;
    }

    public MiniBoard getBoard() {
        return board;
    }
}
