package it.polimi.ingsw.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.BasicAction;
import it.polimi.ingsw.model.minified.*;
import it.polimi.ingsw.util.Json;
import it.polimi.ingsw.util.ResourceException;
import it.polimi.ingsw.util.ResourceManager;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * This class associate the different part of the model with a description
 */
public class Descriptions {
    /**
     * The map of the attack id-description
     */
    private static Map<String, String> attackMap = null;

    /**
     * This class should not be constructed.
     */
    private Descriptions() {}

    static {
        loadAttacks();
    }

    /**
     * Find a model
     * @param model model to find
     * @param uuid UUID of the model
     * @return the string of the model
     */
    public static String find(MiniModel model, String uuid) {
        String res = null;
        for (MiniPowerUp p : model.getMyPowerUps()) {
            if (res == null) {
                res = find(p, uuid);
            }
        }

        for (MiniPowerUp p : model.getMyDrawnPowerUps()) {
            if (res == null) {
                res = find(p, uuid);
            }
        }

        return res == null ? find(model.getMatch(), uuid) : res;
    }

    /**
     * Find a match
     * @param match match to find
     * @param uuid UUID of the match
     * @return the string of the match
     */
    static String find(MiniMatch match, String uuid) {
        String res = null;
        for (MiniAction action : match.getCurrentTurn().getAvaibleActions()) {
            if (res == null) {
                res = find(action, uuid);
            }
        }

        for (MiniPlayer p : match.getPlayers()) {
            if (res == null) {
                res = find(p, uuid);
            }
        }

        return res == null ? find(match.getGameBoard(), uuid) : res;
    }

    /**
     * Find game board
     * @param gameBoard game board to find
     * @param uuid UUID of the game board
     * @return the string of the game board
     */
    static String find(MiniGameBoard gameBoard, String uuid) {
        return find(gameBoard.getBoard(), uuid);
    }

    /**
     * Find board
     * @param board board to find
     * @param uuid UUID of the board
     * @return the string of the board
     */
    static String find(MiniBoard board, String uuid) {
        String res = null;
        for (MiniStandardSquare sq : board.getStandardSquares()) {
            if (res == null) {
                res = find(sq, uuid);
            }
        }

        for (MiniSpawnPoint sq : board.getSpawnPoints()) {
            if (res == null) {
                res = find(sq, uuid);
            }
        }

        return res;
    }

    /**
     * Find the std square
     * @param sq std square to find
     * @param uuid UUID of the std square
     * @return the string of the std square
     */
    static String find(MiniStandardSquare sq, String uuid) {
        return find((MiniSquare)sq, uuid);
    }

    /**
     * Find the spawn point
     * @param sq spawn point to find
     * @param uuid UUID of the spawn point
     * @return string of the spawn point
     */
    static String find(MiniSpawnPoint sq, String uuid) {
        String res = null;
        for (MiniWeapon w : sq.getWeapons()) {
            if (res == null) {
                res = find(w, uuid);
            }
        }

        return res == null ? find((MiniSquare)sq, uuid) : res;
    }

    /**
     * Find the square
     * @param sq square to find
     * @param uuid UUID of the square
     * @return string that describe the square
     */
    static String find(MiniSquare sq, String uuid) {
        if (sq.getUuid().equals(uuid)) {
            return describe(sq);
        }
        return null;
    }

    /**
     * Find a weapon
     * @param w weapon to find
     * @param uuid UUID of the weapon
     * @return string that describe the weapon
     */
    static String find(MiniWeapon w, String uuid) {
        if (w.getUuid().equals(uuid)) {
            return describe(w);
        }
        String res = null;
        for (MiniAttack atk : w.getAttacks()) {
            if (res == null) {
                res = find(atk, uuid);
            }
        }
        return res;
    }

    /**
     * Find a player
     * @param p the player to find
     * @param uuid UUID of the player
     * @return the string that describe the player
     */
    static String find(MiniPlayer p, String uuid) {
        if (p.getUuid().equals(uuid)) {
            return describe(p);
        }

        String res = null;
        for (MiniWeapon w : p.getWeapons()) {
            if (res == null) {
                res = find(w, uuid);
            }
        }

        return res;
    }

    /**
     * Find a action
     * @param action the action to find
     * @param uuid UUID of the action
     * @return string that describe the action
     */
    static String find(MiniAction action, String uuid) {
        if (action.getUuid().equals(uuid)) {
            return describe(action);
        }
        return null;
    }

    /**
     * Find a power-up
     * @param p the power-up to find
     * @param uuid UUID of the power-up
     * @return string that describe the poer-up
     */
    static String find(MiniPowerUp p, String uuid) {
        if (p.getUuid().equals(uuid)) {
            return describe(p);
        }
        return null;
    }

    /**
     * Load the attacks in the the map
     */
    static void loadAttacks() {
        if (attackMap == null) {
            attackMap = new HashMap<>();
            String path = "/weapons/attacks.json";
            ObjectMapper mapper = Json.getMapper();
            try {
                JsonNode json = mapper.readTree(ResourceManager.get(path));
                for (JsonNode atk : json) {
                    String id = atk.get("id").asText();
                    String desc = atk.get("description").asText();
                    attackMap.put(id, desc);
                }
            } catch (Exception e) {
                throw new ResourceException("Cannot load weapon attacks descriptions");
            }
        }
    }

    /**
     * Find a movement
     * @param mov movement to find
     * @param uuid UUID of the movement effect
     * @return string that describe the movement effect
     */
    static String find(MiniMovement mov, String uuid) {
        if (mov.getUuid().equals(uuid)) {
            return describe(mov);
        }
        return null;
    }

    /**
     * Find an attack
     * @param atk attack to find
     * @param uuid UUID of the attack
     * @return string that describe the attack
     */
    static String find(MiniAttack atk, String uuid) {
        if (atk.getUuid().equals(uuid)) {
            return describe(atk);
        }

        if (atk.hasBonusMovement()) {
            return find(atk.getBonusMovement(), uuid);
        } else {
            return null;
        }
    }

    /**
     * Describe a attack
     * @param atk attack to describe
     * @return the description of the attack
     */
    public static String describe(MiniAttack atk) {
        return attackMap.get(atk.getId());
    }

    /**
     * Describe a movement
     * @param mov the movement to describe
     * @return description of the movement
     */
    public static String describe(MiniMovement mov) {
        return "Movimento";
    }

    /**
     * Describe a power-up
     * @param p power-up to describe
     * @return description of the powe-up
     */
    public static String describe(MiniPowerUp p) {
        return MessageFormat.format("Powerup {0} {1}", p.getType(), p.getAmmo());
    }

    /**
     * Describe an action
     * @param action the action to describe
     * @return description of the action
     */
    public static String describe(MiniAction action) {
        StringBuilder sb = new StringBuilder();
        sb.append("Azione");
        for (BasicAction ba : action.getActions()) {
            switch (ba) {
                case MOVEMENT:
                    sb.append(" muovi");
                    break;
                case GRAB:
                    sb.append(" raccogli");
                    break;
                case RELOAD:
                    sb.append(" ricarica");
                    break;
                case SHOOT:
                    sb.append(" spara");
                    break;
                case SKIP:
                    sb.append(" salta azione");
                    break;
                case POWERUP:
                    sb.append(" powerup");
            }
        }

        return sb.toString();
    }

    /**
     * Describe a player
     * @param p player to describe
     * @return description of the player
     */
    public static String describe(MiniPlayer p) {
        return MessageFormat.format("Giocatore {0}", p.getNickname());
    }

    /**
     * Describe a square
     * @param sq the square to describe
     * @return description of the square
     */
    public static String describe(MiniSquare sq) {
        return MessageFormat.format(
                "Posizione ({0}, {1})",
                sq.getCoordinates().getRow()+1,
                sq.getCoordinates().getColumn()+1);
    }

    /**
     * Describe a weapon
     * @param w the weapon to describe
     * @return description of the weapon
     */
    public static String describe(MiniWeapon w) {
        return w.getName();
    }
}
