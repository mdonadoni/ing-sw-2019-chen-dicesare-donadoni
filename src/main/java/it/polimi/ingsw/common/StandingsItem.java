package it.polimi.ingsw.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.PlayerToken;

import java.io.Serializable;

/**
 * This class is used to get standings of the players
 */
public class StandingsItem implements Serializable {
    /**
     * The UID.
     */
    private static final long serialVersionUID = -2919952378041208852L;
    /**
     * Position in the standings.
     */
    private int position;
    /**
     * Nickname of the player.
     */
    private String nickname;
    /**
     * The points of the player.
     */
    private int points;
    /**
     * The color of the player.
     */
    private PlayerToken color;

    @JsonCreator
    private StandingsItem() {}

    /**
     * Constructor of the class
     * @param position Position in the standings.
     * @param nickname Nickname of the player.
     * @param points The points of the player.
     * @param color The color of the player.
     */
    public StandingsItem(int position, String nickname, int points, PlayerToken color) {
        this.position = position;
        this.points = points;
        this.nickname = nickname;
        this.color = color;
    }

    public int getPosition() {
        return position;
    }

    public String getNickname() {
        return nickname;
    }

    public int getPoints() {
        return points;
    }

    public PlayerToken getColor() {
        return color;
    }
}
