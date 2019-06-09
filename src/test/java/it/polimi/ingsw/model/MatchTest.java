package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {
    private Match match;
    private Player playerA;
    private Player playerB;
    private Player playerC;
    private Player playerD;
    private Player playerE;

    @BeforeEach
    void setupPlayers() {
        match = new Match(
                Arrays.asList("A", "B", "C", "D", "E"),
                new JsonModelFactory(BoardType.SMALL)
        );

        Function<String, Player> getByNickname = (name) -> {
            for (Player p : match.getPlayers()) {
                if (p.getNickname().equals(name)) {
                    return p;
                }
            }
            return null;
        };
        playerA = match.getPlayerByNickname("A");
        playerB = match.getPlayerByNickname("B");
        playerC = match.getPlayerByNickname("C");
        playerD = match.getPlayerByNickname("D");
        playerE = match.getPlayerByNickname("E");
    }

    @Test
    void nextTurn() {
        assertEquals(match.getCurrentTurn().getCurrentPlayer(), playerA);
        match.nextTurn();
        assertEquals(match.getCurrentTurn().getCurrentPlayer(), playerB);
        match.nextTurn();
        assertEquals(match.getCurrentTurn().getCurrentPlayer(), playerC);
        match.nextTurn();
        assertEquals(match.getCurrentTurn().getCurrentPlayer(), playerD);
        match.nextTurn();
        assertEquals(match.getCurrentTurn().getCurrentPlayer(), playerE);
        match.nextTurn();
        assertEquals(match.getCurrentTurn().getCurrentPlayer(), playerA);
    }

    @Test
    void isActive() {
        assertTrue(match.isActive());
        playerA.setActive(false);
        playerD.setActive(false);
        assertTrue(match.isActive());
        playerE.setActive(false);
        assertFalse(match.isActive());
        playerD.setActive(true);
        assertTrue(match.isActive());

    }

    @Test
    void getOtherPlayers() {
        assertFalse(match.getOtherPlayers("A").contains(playerA));
        assertFalse(match.getOtherPlayers("C").contains(playerC));
        assertFalse(match.getOtherPlayers("E").contains(playerE));
    }
}