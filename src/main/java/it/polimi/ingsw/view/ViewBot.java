package it.polimi.ingsw.view;

import it.polimi.ingsw.model.PowerUp;
import it.polimi.ingsw.model.minified.*;
import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.network.LocalView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

public class ViewBot extends LocalView implements Runnable {

    private static final Random RAND = new Random();

    private MiniModel model;

    private static int randInt(int a, int b) {
        int delta = b-a+1;
        int rand = RAND.nextInt(delta);
        return a + rand;
    }

    @Override
    public synchronized ArrayList<String> selectObject(ArrayList<String> objUuid, int min, int max) {
        System.out.println("SELECT: " + objUuid + " min " + min + " max " +  max);

        // Check if MiniModel actually has what we need to search for
        for (String uuid : objUuid) {
            boolean found = false;
            for (PowerUp p : model.getMyDrawnPowerUps()) {
                found = found || p.getUuid().equals(uuid);
            }
            for (PowerUp p : model.getMyPowerUps()) {
                found = found || p.getUuid().equals(uuid);
            }

            MiniMatch match = model.getMatch();
            for (MiniAction a : match.getCurrentTurn().getAvaibleActions()) {
                found = found || a.getUuid().equals(uuid);
            }

            MiniBoard board = match.getGameBoard().getBoard();
            for (MiniSpawnPoint s : board.getSpawnPoints()) {
                found = found || s.getUuid().equals(uuid);
                for (MiniWeapon w : s.getWeapons()) {
                    found = found || w.getUuid().equals(uuid);
                }
            }
            for (MiniStandardSquare s : board.getStandardSquares()) {
                found = found || s.getUuid().equals(uuid);
            }

            for (MiniPlayer p : match.getPlayers()) {
                found = found || p.getUuid().equals(uuid);
                for (MiniWeapon w : p.getWeapons()) {
                    found = found || w.getUuid().equals(uuid);
                }
            }

            found = found || model.getMyMiniPlayer().getUuid().equals(uuid);

            if (!found) {
                System.out.println("ERROR " + uuid);
                System.exit(1);
            }
        }

        ArrayList<String> shuffled = new ArrayList<>(objUuid);
        Collections.shuffle(shuffled, RAND);

        // TODO remove these lines whene everything is fixed
        max = Math.min(max, shuffled.size());
        min = Math.min(min, max);

        ArrayList<String> res = new ArrayList<>(shuffled.subList(0, randInt(min, max)));
        System.out.println("SELEZIONATO: " + res);
        return res;
    }

    @Override
    public void showMessage(String message) {
        System.out.println("MESSAGE: " + message);
    }

    @Override
    public synchronized void updateModel(MiniModel model) {
        System.out.println("NEW MODEL");
        this.model = model;
    }

    @Override
    public void run() {
        try {
            connectServer("localhost", 1099, ConnectionType.RMI);
            String name = UUID.randomUUID().toString().substring(0, 5);
            System.out.println("IO SONO " + name);
            getServer().login(name, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
