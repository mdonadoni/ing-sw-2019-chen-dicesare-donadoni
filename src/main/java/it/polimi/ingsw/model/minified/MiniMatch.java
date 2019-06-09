package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.Identifiable;
import it.polimi.ingsw.model.Match;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MiniMatch extends Identifiable implements Serializable {
    private static final long serialVersionUID = 8601733222249808278L;

    private final ArrayList<MiniPlayer> players;
    private final MiniGameBoard gameBoard;
    private final MiniTurn currentTurn;

    @JsonCreator
    private MiniMatch() {
        this.players = null;
        this.gameBoard = null;
        this.currentTurn = null;
    }

    public MiniMatch(Match match) {
        super(match.getUuid());
        this.players = new ArrayList<>();
        match.getPlayers().forEach((p) -> players.add(new MiniPlayer(p)));
        this.gameBoard = new MiniGameBoard(match.getGameBoard());
        this.currentTurn = new MiniTurn(match.getCurrentTurn().getCurrentPlayer(), match.getFinalFrenzy());
    }

    public List<MiniPlayer> getPlayers() {
        return new ArrayList<>(players);
    }

    public MiniGameBoard getGameBoard() {
        return gameBoard;
    }

    public MiniTurn getCurrentTurn() {
        return currentTurn;
    }
}
