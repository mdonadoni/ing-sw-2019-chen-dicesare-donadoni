package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {

    GameBoard gb;
    List<PlayerToken> singleYellow;
    List<PlayerToken> singleGreen;
    List<PlayerToken> doublePurple;
    List<PlayerToken> doubleYellow;
    List<PlayerToken> singleBlue;
    List<PlayerToken> singleGrey;
    List<PlayerToken> tetraGrey;
    List<PlayerToken> tripleBlue;

    private void initGb() throws ResourceException{
        gb = new GameBoard(5);

        singleYellow = new ArrayList<>();
        singleYellow.add(PlayerToken.YELLOW);
        singleGreen = new ArrayList<>();
        singleGreen.add(PlayerToken.GREEN);
        doublePurple = new ArrayList<>();
        doublePurple.add(PlayerToken.PURPLE);
        doublePurple.add(PlayerToken.PURPLE);
        doubleYellow = new ArrayList<>();
        doubleYellow.add(PlayerToken.YELLOW);
        doubleYellow.add(PlayerToken.YELLOW);
        singleBlue = new ArrayList<>();
        singleBlue.add(PlayerToken.BLUE);
        singleGrey = new ArrayList<>();
        singleGrey.add(PlayerToken.GREY);
        tetraGrey = new ArrayList<>();
        tetraGrey.add(PlayerToken.GREY);
        tetraGrey.add(PlayerToken.GREY);
        tetraGrey.add(PlayerToken.GREY);
        tetraGrey.add(PlayerToken.GREY);
        tripleBlue = new ArrayList<>();
        tripleBlue.add(PlayerToken.BLUE);
        tripleBlue.add(PlayerToken.BLUE);
        tripleBlue.add(PlayerToken.BLUE);
    }

    @Test
    void addKill() throws ResourceException{
        initGb();

        gb.addKill(singleYellow); // 0
        gb.addKill(doublePurple); // 1
        gb.addKill(doubleYellow); // 2
        gb.addKill(singleGreen); // 3
        gb.addKill(singleBlue); // 4
        gb.addKill(tetraGrey); // 5
        gb.addKill(tripleBlue); // 5

        assertEquals(0, gb.getRemainingSkulls());
        assertEquals(7, gb.getKillShotTrack().get(5).size());
        assertSame(gb.getKillShotTrack().get(5).get(3), PlayerToken.GREY);
        assertSame(gb.getKillShotTrack().get(2).get(1), PlayerToken.YELLOW);
        assertEquals(2, gb.getKillShotTrack().get(2).size());
    }

    @Test
    void countKills() throws ResourceException{
        initGb();

        gb.addKill(tripleBlue); // 0
        gb.addKill(singleYellow); // 1
        gb.addKill(tetraGrey); // 2
        gb.addKill(singleBlue); // 3
        gb.addKill(doubleYellow); // 4
        gb.addKill(singleGreen); // 5
        gb.addKill(singleGreen); // 5
        gb.addKill(tripleBlue); // 5

        assertEquals(7, gb.countKills(PlayerToken.BLUE));
        assertEquals(3, gb.countKills(PlayerToken.YELLOW));
        assertEquals(2, gb.countKills(PlayerToken.GREEN));
        assertEquals(4, gb.countKills(PlayerToken.GREY));
        assertEquals(0, gb.countKills(PlayerToken.PURPLE));
    }

    @Test
    void getKillShotTrackOrder() throws ResourceException{
        initGb();

        gb.addKill(singleGreen); // 0
        gb.addKill(doublePurple); // 1
        gb.addKill(doublePurple); // 2
        gb.addKill(singleGrey); // 3
        gb.addKill(singleBlue); // 4
        gb.addKill(singleGrey); // 5
        gb.addKill(doubleYellow); // 5

        List<PlayerToken> order = gb.getKillShotTrackOrder();
        assertSame(order.get(0), PlayerToken.PURPLE);
        assertSame(order.get(1), PlayerToken.GREY);
        assertSame(order.get(2), PlayerToken.YELLOW);
        assertSame(order.get(3), PlayerToken.GREEN);
        assertSame(order.get(4), PlayerToken.BLUE);
    }
}