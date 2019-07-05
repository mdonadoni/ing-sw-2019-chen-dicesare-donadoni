package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.weapons.Weapon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Player for the client.
 */
public class MiniPlayer extends MiniIdentifiable implements Serializable {
    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = -6231900211873610005L;
    /**
     * Nickname of the player
     */
    private final String nickname;
    /**
     * Number of skulls this player has.
     */
    private final int skulls;
    /**
     * Whether the player is active or not.
     */
    private final boolean active;
    /**
     * Whether this player board is flipped or not.
     */
    private final boolean boardFlipped;
    /**
     * Color of this player.
     */
    private final PlayerToken color;
    /**
     * Marks this player has.
     */
    private final ArrayList<PlayerToken> marks;
    /**
     * Damage taken by this player.
     */
    private final ArrayList<PlayerToken> damageTaken;
    /**
     * Weapons of this player.
     */
    private final ArrayList<MiniWeapon> weapons;
    /**
     * Ammunitions of this player.
     */
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

    /**
     * Constructor of MiniPlayer from a full Player.
     * @param player Player to be copied.
     */
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

    /**
     * Get the nickname of this player.
     * @return Nickname of this player.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Get the number of skulls this player has.
     * @return Number of skulls.
     */
    public int getSkulls() {
        return skulls;
    }

    /**
     * Get whether this player is active or not.
     * @return True if player is active, otherwise false.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Get whether the board is flipped or not.
     * @return True if board is flipped, otherwise false.
     */
    public boolean isBoardFlipped() {
        return boardFlipped;
    }

    /**
     * Get the color of this player.
     * @return Color of this player.
     */
    public PlayerToken getColor() {
        return color;
    }

    /**
     * Get the marks that this player has.
     * @return Marks of this player.
     */
    public List<PlayerToken> getMarks() {
        return marks;
    }

    /**
     *Get the list of damage this player has taken.
     * @return Damage of this player.
     */
    public List<PlayerToken> getDamageTaken() {
        return damageTaken;
    }

    /**
     * Get the list of weapons of this player.
     * @return List of weapons.
     */
    public List<MiniWeapon> getWeapons() {
        return weapons;
    }

    /**
     * Get the ammos of this player.
     * @return Ammos of this player.
     */
    public List<AmmoColor> getAmmo() {
        return ammo;
    }
}
