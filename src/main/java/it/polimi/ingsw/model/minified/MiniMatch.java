package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.Match;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Match for the client side.
 */
public class MiniMatch extends MiniIdentifiable implements Serializable {
    /**
     * Serializable ID.
     */
    private static final long serialVersionUID = 8601733222249808278L;
    /**
     * List of players in this match.
     */
    private final ArrayList<MiniPlayer> players;
    /**
     * Game board of this match.
     */
    private final MiniGameBoard gameBoard;
    /**
     * Informations regarding current turn of the game.
     */
    private final MiniTurn currentTurn;

    @JsonCreator
    private MiniMatch() {
        this.players = null;
        this.gameBoard = null;
        this.currentTurn = null;
    }

    /**
     * Constructor of MiniMatch from full Match.
     * @param match Match to be copied.
     */
    public MiniMatch(Match match) {
        super(match.getUuid());
        this.players = new ArrayList<>();
        match.getPlayers().forEach(p -> players.add(new MiniPlayer(p)));
        this.gameBoard = new MiniGameBoard(match.getGameBoard());
        this.currentTurn = new MiniTurn(match.getCurrentTurn().getCurrentPlayer(), match.getFinalFrenzy());
    }

    /**
     * Get the list of players.
     * @return List of players.
     */
    public List<MiniPlayer> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * Get the gameboard of this Match.
     * @return The gameboard of this match.
     */
    public MiniGameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Get the informations about the current turn.
     * @return Current turn of this game.
     */
    public MiniTurn getCurrentTurn() {
        return currentTurn;
    }
}
