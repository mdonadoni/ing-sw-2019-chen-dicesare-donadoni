package it.polimi.ingsw.view;

import it.polimi.ingsw.common.StandingsItem;
import it.polimi.ingsw.common.dialogs.Dialog;
import it.polimi.ingsw.model.minified.MiniModel;
import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.network.LocalView;

import java.text.MessageFormat;
import java.util.*;

public class ViewBot extends LocalView implements Runnable {

    private static final Random RAND = new Random();

    private MiniModel model;

    private static int randInt(int a, int b) {
        int delta = b-a+1;
        int rand = RAND.nextInt(delta);
        return a + rand;
    }

    @Override
    public synchronized ArrayList<String> selectObject(ArrayList<String> objUuid, int min, int max, Dialog dialog) {
        System.out.println("SELECT: " + objUuid + " min " + min + " max " +  max);

        // Check if MiniModel actually has what we need to search for
        for (String uuid : objUuid) {
            String desc = Descriptions.find(model, uuid);
            if (desc == null) {
                System.out.println("ERROR " + uuid);
                System.exit(1);
            }

            System.out.println(uuid + " " + desc);
        }

        ArrayList<String> shuffled = new ArrayList<>(objUuid);
        Collections.shuffle(shuffled, RAND);

        if (min > max || min < 0 || max > objUuid.size()) {
            System.out.println("ERROR wrong min or max");
            System.exit(2);
        }

        ArrayList<String> res = new ArrayList<>(shuffled.subList(0, randInt(min, max)));
        System.out.println("SELEZIONATO: " + res);
        return res;
    }

    @Override
    public synchronized void showMessage(String message) {
        System.out.println("MESSAGE: " + message);
    }

    @Override
    public synchronized void updateModel(MiniModel model) {
        System.out.println("NEW MODEL");
        this.model = model;
    }

    @Override
    public synchronized void notifyEndMatch(ArrayList<StandingsItem> standings) {
        System.out.println("FINE MATCH");
        for (StandingsItem s : standings) {
            System.out.println(
                    MessageFormat.format(
                            "{0}) {1} ({2} punti)",
                            s.getPosition(),
                            s.getNickname(),
                            s.getPoints()));
        }
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
