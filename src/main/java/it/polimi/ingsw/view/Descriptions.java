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

public class Descriptions {
    private static Map<String, String> attackMap = null;

    /**
     * This class should not be constructed.
     */
    private Descriptions() {}

    static {
        loadAttacks();
    }

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

    static String find(MiniGameBoard gameBoard, String uuid) {
        return find(gameBoard.getBoard(), uuid);
    }

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

    static String find(MiniStandardSquare sq, String uuid) {
        return find((MiniSquare)sq, uuid);
    }

    static String find(MiniSpawnPoint sq, String uuid) {
        String res = null;
        for (MiniWeapon w : sq.getWeapons()) {
            if (res == null) {
                res = find(w, uuid);
            }
        }

        return res == null ? find((MiniSquare)sq, uuid) : res;
    }

    static String find(MiniSquare sq, String uuid) {
        if (sq.getUuid().equals(uuid)) {
            return describe(sq);
        }
        return null;
    }

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

    static String find(MiniAction action, String uuid) {
        if (action.getUuid().equals(uuid)) {
            return describe(action);
        }
        return null;
    }

    static String find(MiniPowerUp p, String uuid) {
        if (p.getUuid().equals(uuid)) {
            return describe(p);
        }
        return null;
    }


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

    static String find(MiniMovement mov, String uuid) {
        if (mov.getUuid().equals(uuid)) {
            return describe(mov);
        }
        return null;
    }

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

    public static String describe(MiniAttack atk) {
        return attackMap.get(atk.getId());
    }

    public static String describe(MiniMovement mov) {
        return "Movimento";
    }

    public static String describe(MiniPowerUp p) {
        return MessageFormat.format("Powerup {0} {1}", p.getType(), p.getAmmo());
    }

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

    public static String describe(MiniPlayer p) {
        return MessageFormat.format("Giocatore {0}", p.getNickname());
    }

    public static String describe(MiniSquare sq) {
        return MessageFormat.format(
                "Posizione ({0}, {1})",
                sq.getCoordinates().getRow()+1,
                sq.getCoordinates().getColumn()+1);
    }

    public static String describe(MiniWeapon w) {
        return w.getName();
    }
}
