package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.weapons.Weapon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MiniPlayer extends Identifiable implements Serializable {

    private static final long serialVersionUID = -6231900211873610005L;
    private final String nickname;
    private final int skulls;
    private final boolean active;
    private final boolean boardFlipped;
    private final PlayerToken color;
    private final ArrayList<PlayerToken> marks;
    private final ArrayList<PlayerToken> damageTaken;
    private final ArrayList<MiniWeapon> weapons;
    private final ArrayList<AmmoColor> ammo;

    @JsonCreator
    private MiniPlayer() {
        nickname = null;
        skulls = 0;
        active = true;
        boardFlipped = false;
        color = null;
        marks = null;
        damageTaken = null;
        weapons = null;
        ammo = null;
    }

    public MiniPlayer(Player player) {
        super(player.getUuid());
        this.nickname = player.getNickname();
        this.skulls = player.getSkulls();
        this.active = player.isActive();
        this.boardFlipped = player.isBoardFlipped();
        this.color = player.getColor();
        this.marks = new ArrayList<>(player.getMarks());
        this.damageTaken = new ArrayList<>(player.getDamageTaken());
        this.weapons = new ArrayList<>();
        for (Weapon w : player.getWeapons()) {
            this.weapons.add(new MiniWeapon(w));
        }
        this.ammo = new ArrayList<>(player.getAmmo());
    }

    public String getNickname() {
        return nickname;
    }

    public int getSkulls() {
        return skulls;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isBoardFlipped() {
        return boardFlipped;
    }

    public PlayerToken getColor() {
        return color;
    }

    public List<PlayerToken> getMarks() {
        return marks;
    }

    public List<PlayerToken> getDamageTaken() {
        return damageTaken;
    }

    public List<MiniWeapon> getWeapons() {
        return weapons;
    }

    public List<AmmoColor> getAmmo() {
        return ammo;
    }
}
