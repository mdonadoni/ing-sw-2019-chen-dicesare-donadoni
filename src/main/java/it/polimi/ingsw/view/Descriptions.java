package it.polimi.ingsw.view;

import it.polimi.ingsw.model.BasicAction;
import it.polimi.ingsw.model.PowerUp;
import it.polimi.ingsw.model.minified.*;

import java.text.MessageFormat;

public class Descriptions {
    public static String find(MiniModel model, String uuid) {
        String res = null;
        for (PowerUp p : model.getMyPowerUps()) {
            if (res == null) {
                res = find(p, uuid);
            }
        }

        for (PowerUp p : model.getMyDrawnPowerUps()) {
            if (res == null) {
                res = find(p, uuid);
            }
        }

        return res == null ? find(model.getMatch(), uuid) : res;
    }

    private static String find(MiniMatch match, String uuid) {
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

    private static String find(MiniGameBoard gameBoard, String uuid) {
        return find(gameBoard.getBoard(), uuid);
    }

    private static String find(MiniBoard board, String uuid) {
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

    private static String find(MiniStandardSquare sq, String uuid) {
        return find((MiniSquare)sq, uuid);
    }

    private static String find(MiniSpawnPoint sq, String uuid) {
        String res = null;
        for (MiniWeapon w : sq.getWeapons()) {
            if (res == null) {
                res = find(w, uuid);
            }
        }

        return res == null ? find((MiniSquare)sq, uuid) : res;
    }

    private static String find(MiniSquare sq, String uuid) {
        if (sq.getUuid().equals(uuid)) {
            return MessageFormat.format(
                    "Posizione ({0}, {1})",
                    sq.getCoordinates().getRow(),
                    sq.getCoordinates().getColumn());
        }
        return null;
    }

    private static String find(MiniWeapon w, String uuid) {
        if (w.getUuid().equals(uuid)) {
            return w.getName();
        }
        return null;
    }

    private static String find(MiniPlayer p, String uuid) {
        if (p.getUuid().equals(uuid)) {
            return MessageFormat.format("Giocatore {0}", p.getNickname());
        }

        String res = null;
        for (MiniWeapon w : p.getWeapons()) {
            if (res == null) {
                res = find(w, uuid);
            }
        }

        return res;
    }

    private static String find(MiniAction action, String uuid) {
        String res = null;
        if (action.getUuid().equals(uuid)) {
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

            res = sb.toString();
        }
        return res;
    }

    private static String find(PowerUp p, String uuid) {
        if (p.getUuid().equals(uuid)) {
            return MessageFormat.format("Powerup {0} {1}", p.getType(), p.getAmmo());
        }
        return null;
    }


}
