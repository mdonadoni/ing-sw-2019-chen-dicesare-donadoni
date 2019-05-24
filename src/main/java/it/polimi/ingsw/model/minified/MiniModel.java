package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PowerUp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MiniModel implements Serializable {
    private static final long serialVersionUID = -5949126643305556688L;

    private final MiniMatch match;
    private final String myNickname;
    private final ArrayList<PowerUp> myPowerUps;
    private final int myPoints;

    @JsonCreator
    private MiniModel() {
        this.match = null;
        this.myNickname = null;
        this.myPowerUps = null;
        this.myPoints = 0;
    }

    public MiniModel(Match match, Player player) {
        this.match = new MiniMatch(match);
        this.myNickname = player.getNickname();
        this.myPowerUps = new ArrayList<>(player.getPowerUps());
        this.myPoints = player.getPoints();
    }

    public MiniMatch getMatch() {
        return match;
    }

    public List<PowerUp> getMyPowerUps() {
        return new ArrayList<>(myPowerUps);
    }

    public int getMyPoints() {
        return myPoints;
    }

    public String getMyNickname() {
        return myNickname;
    }
}
