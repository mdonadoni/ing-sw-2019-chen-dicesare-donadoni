package it.polimi.ingsw.model;

import it.polimi.ingsw.model.PlayerToken;
import java.util.Comparator;
import java.util.List;

public class HistorySorter implements Comparator<PlayerToken> {

    private List<PlayerToken> history;

    private int countKills(PlayerToken player){
        int result = 0;

        for(PlayerToken token : history)
            if(token == player)
                result++;

         return result;
    }

    private int whocomesfirst(PlayerToken p1, PlayerToken p2){
        int p1ndx = history.indexOf(p1);
        int p2ndx = history.indexOf(p2);
        int result = 0;

        if(p1ndx < p2ndx)
            result = 1;

        if(p2ndx < p1ndx)
            result = -1;

        return result;
    }

    public HistorySorter(List<PlayerToken> history){
        this.history = history;
    }

    public int compare(PlayerToken p1, PlayerToken p2){
        if(countKills(p1) > countKills(p2))
            return 1;
        else if(countKills(p2) > countKills(p1))
            return -1;
        else
            return whocomesfirst(p1, p2);
    }
}
