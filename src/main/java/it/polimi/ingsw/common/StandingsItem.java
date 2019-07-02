package it.polimi.ingsw.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.PlayerToken;

import java.io.Serializable;

public class StandingsItem implements Serializable {
    private static final long serialVersionUID = -2919952378041208852L;
    private int position;
    private String nickname;
    private int points;
    private PlayerToken color;

    @JsonCreator
    private StandingsItem() {}

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
