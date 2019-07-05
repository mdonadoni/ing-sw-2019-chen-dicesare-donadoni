package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Model for the client. Every player has a customized MiniModel based on what
 * he can see in the game. By doing it this way we can ensure no one is cheating.
 * This MiniModel is made of very simple and immutable components that are designed
 * to be easy to be serialized and deserialized, thus making this ideal to be sent
 * over a network.
 */
public class MiniModel implements Serializable {
    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = -5949126643305556688L;
    /**
     * Match of this game.
     */
    private final MiniMatch match;
    /**
     * Nickname of the player receiving this reduced model.
     */
    private final String myNickname;
    /**
     * Powerups of the player receiving this reduced model. Only the player
     * receiving this model can see his powerups.
     */
    private final ArrayList<MiniPowerUp> myPowerUps;
    /**
     * Powerups drawn by the player receiving this model.
     */
    private final ArrayList<MiniPowerUp> myDrawnPowerUps;
    /**
     * Points of the player receiving this model.
     */
    private final int myPoints;

    @JsonCreator
    private MiniModel() {
        this.match = null;
        this.myNickname = null;
        this.myPowerUps = null;
        this.myDrawnPowerUps = null;
        this.myPoints = 0;
    }

    /**
     * Constructor of MiniModel from a full match and knowing the player that
     * will be receiving this model.
     * @param match Match to be copied.
     * @param player Receiver of this model.
     */
    public MiniModel(Match match, Player player) {
        this.match = new MiniMatch(match);
        this.myNickname = player.getNickname();
        this.myPowerUps = new ArrayList<>();
        player.getPowerUps().forEach(p -> myPowerUps.add(new MiniPowerUp(p)));
        this.myDrawnPowerUps = new ArrayList<>();
        player.getDrawnPowerUps().forEach(p -> myDrawnPowerUps.add(new MiniPowerUp(p)));
        this.myPoints = player.getTotalPoints();
    }

    /**
     * Get Match of this model.
     * @return The match of this model.
     */
    public MiniMatch getMatch() {
        return match;
    }

    /**
     * Get powerups of the player receiving this model.
     * @return Powerups of the player.
     */
    public List<MiniPowerUp> getMyPowerUps() {
        return new ArrayList<>(myPowerUps);
    }

    /**
     * Get the powerups drawn by the receiving player.
     * @return Drawn powerups of this player.
     */
    public List<MiniPowerUp> getMyDrawnPowerUps() {
        return myDrawnPowerUps;
    }

    /**
     * Get the points of the player receiving this model.
     * @return The points of this player.
     */
    public int getMyPoints() {
        return myPoints;
    }

    /**
     * Get the nickname of the player receiving this model.
     * @return Nickname of this player.
     */
    public String getMyNickname() {
        return myNickname;
    }

    /**
     * Get the actual MiniPlayer object of the player receiving this model.
     * @return MiniPlayer of this player.
     */
    public MiniPlayer getMyMiniPlayer(){
        return match.getPlayers().stream()
                .filter(pl -> pl.getNickname().equals(getMyNickname()))
                .collect(Collectors.toList())
                .get(0);
    }
}
