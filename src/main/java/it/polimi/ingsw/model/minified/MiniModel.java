package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MiniModel implements Serializable {
    private static final long serialVersionUID = -5949126643305556688L;

    private final MiniMatch match;
    private final String myNickname;
    private final ArrayList<MiniPowerUp> myPowerUps;
    private final ArrayList<MiniPowerUp> myDrawnPowerUps;
    private final int myPoints;

    @JsonCreator
    private MiniModel() {
        this.match = null;
        this.myNickname = null;
        this.myPowerUps = null;
        this.myDrawnPowerUps = null;
        this.myPoints = 0;
    }

    public MiniModel(Match match, Player player) {
        this.match = new MiniMatch(match);
        this.myNickname = player.getNickname();
        this.myPowerUps = new ArrayList<>();
        player.getPowerUps().forEach(p -> myPowerUps.add(new MiniPowerUp(p)));
        this.myDrawnPowerUps = new ArrayList<>();
        player.getDrawnPowerUps().forEach(p -> myDrawnPowerUps.add(new MiniPowerUp(p)));
        this.myPoints = player.getTotalPoints();
    }

    public MiniMatch getMatch() {
        return match;
    }

    public List<MiniPowerUp> getMyPowerUps() {
        return new ArrayList<>(myPowerUps);
    }

    public ArrayList<MiniPowerUp> getMyDrawnPowerUps() {
        return myDrawnPowerUps;
    }

    public int getMyPoints() {
        return myPoints;
    }

    public String getMyNickname() {
        return myNickname;
    }

    public MiniPlayer getMyMiniPlayer(){
        return match.getPlayers().stream()
                .filter(pl -> pl.getNickname().equals(getMyNickname()))
                .collect(Collectors.toList())
                .get(0);
    }
}
