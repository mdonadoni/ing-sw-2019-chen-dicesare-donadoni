package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {
    private Match match;
    private Player playerA = new Player("Anna", PlayerToken.YELLOW);
    private Player playerB = new Player("Bobby", PlayerToken.YELLOW);
    private Player playerC = new Player("Charles", PlayerToken.YELLOW);
    private Player playerD = new Player("Dante", PlayerToken.YELLOW);
    private Player playerE = new Player("Edd", PlayerToken.YELLOW);

    @BeforeEach
    void setupPlayers() throws ResourceException{
        match = new Match();
        match.addPlayer(playerA);
        match.addPlayer(playerB);
        match.addPlayer(playerC);
        match.addPlayer(playerD);
        match.addPlayer(playerE);
    }

    @Test
    void nextTurn() {
        match.startMatch();
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
}