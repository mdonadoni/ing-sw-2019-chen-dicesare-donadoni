package it.polimi.ingsw.controller;

import it.polimi.ingsw.common.StandingsItem;
import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoreControllerTest {

    ScoreController controller;

    Match match;
    PlayerToken bedaColor;
    PlayerToken adaColor;
    PlayerToken cosoColor;
    PlayerToken debianColor;
    Player bedaPlayer;
    Player adaPlayer;
    Player cosoPlayer;
    Player debianPlayer;

    @BeforeEach
    void setup(){
        List<String> nicks = new ArrayList<>();
        nicks.add("Ada");
        nicks.add("Beda");
        nicks.add("Coso");
        nicks.add("Debian");

        match = new Match(nicks, new JsonModelFactory(BoardType.SMALL));
        bedaPlayer = match.getPlayerByNickname("Beda");
        bedaColor = bedaPlayer.getColor();
        adaPlayer = match.getPlayerByNickname("Ada");
        adaColor = adaPlayer.getColor();
        cosoPlayer = match.getPlayerByNickname("Coso");
        cosoColor = cosoPlayer.getColor();
        debianPlayer = match.getPlayerByNickname("Debian");
        debianColor = debianPlayer.getColor();

        controller = new ScoreController(match);
    }

    @Test
    void singleSimpleKill() {
        bedaPlayer.move(match.getGameBoard().getBoard().getSpawnPointByColor(AmmoColor.RED));
        bedaPlayer.addDamageWithoutMarks(adaColor, 4);
        bedaPlayer.addDamageWithoutMarks(cosoColor, 2);
        bedaPlayer.addDamageWithoutMarks(adaColor, 4);
        bedaPlayer.addDamageWithoutMarks(debianColor, 1);

        controller.lookForScoreUpdates();

        // Should have 8 + 1 points
        assertEquals(adaPlayer.getTotalPoints(), 9);
        // Second damage dealer: 6 points
        assertEquals(cosoPlayer.getTotalPoints(), 6);
        // Last one, but should also have a token on the kill track
        assertEquals(debianPlayer.getTotalPoints(), 4);
        assertTrue(match.getGameBoard().getKillShotTrackOrder().contains(debianColor));

        assertFalse(bedaPlayer.isDead());

        assertNull(bedaPlayer.getSquare());
    }

    @Test
    void singleKillOverkill(){
        adaPlayer.addSkull();

        cosoPlayer.addDamageWithoutMarks(adaColor, 2);
        cosoPlayer.addDamageWithoutMarks(debianColor, 3);

        bedaPlayer.addDamageWithoutMarks(debianColor, 1);

        adaPlayer.addDamageWithoutMarks(bedaColor, 4);
        adaPlayer.addDamageWithoutMarks(bedaColor, 1);
        adaPlayer.addDamageWithoutMarks(debianColor, 3);
        adaPlayer.addDamageWithoutMarks(cosoColor, 2);
        adaPlayer.addDamageWithoutMarks(bedaColor, 2);

        controller.lookForScoreUpdates();

        // Only Ada is dead, so only her board should be cleaned
        // Beda must have achieved 6 + 1 points and a mark from Ada
        assertEquals(bedaPlayer.getTotalPoints(), 7);
        assertEquals(bedaPlayer.getMarks().get(0), adaColor);

        // Then we have Debian with 4 points
        assertEquals(debianPlayer.getTotalPoints(), 4);

        // Finally Coso with 2 points
        assertEquals(cosoPlayer.getTotalPoints(), 2);
    }

    @Test
    void hopeDoesntOverflow(){
        for(int i=0; i<8; i++)
            adaPlayer.addSkull();

        adaPlayer.addDamageWithoutMarks(debianColor, 4);
        adaPlayer.addDamageWithoutMarks(cosoColor,4);
        adaPlayer.addDamageWithoutMarks(bedaColor, 4);

        controller.lookForScoreUpdates();

        // Everyone 1 point except Debian who has also the first blood
        assertEquals(debianPlayer.getTotalPoints(), 2);
        assertEquals(cosoPlayer.getTotalPoints(), 1);
        assertEquals(bedaPlayer.getTotalPoints(), 1);
        assertEquals(adaPlayer.getTotalPoints(), 0);
    }

    @Test
    void multikillTest(){
        bedaPlayer.addDamageWithoutMarks(adaColor, 11);
        cosoPlayer.addDamageWithoutMarks(adaColor, 11);

        controller.lookForScoreUpdates();

        // Ada should have 8 + 1 + 8 + 1 + 1
        assertEquals(adaPlayer.getTotalPoints(), 19);
    }

    @Test
    void finalStandings() {
        adaPlayer.addPoints(5);
        adaPlayer.addKillShotTrackPoints(2);

        bedaPlayer.addPoints(1);
        bedaPlayer.addKillShotTrackPoints(3);

        debianPlayer.addPoints(1);
        debianPlayer.addKillShotTrackPoints(3);

        cosoPlayer.addPoints(2);
        cosoPlayer.addKillShotTrackPoints(2);

        // I expect ada first, beda and debian second, coso fourth
        List<StandingsItem> standings = controller.getFinalStandings();
        assertEquals(adaPlayer.getNickname(), standings.get(0).getNickname());
        assertEquals(cosoPlayer.getNickname(), standings.get(3).getNickname());

        assertEquals(1, standings.get(0).getPosition());
        assertEquals(2, standings.get(1).getPosition());
        assertEquals(2, standings.get(2).getPosition());
        assertEquals(4, standings.get(3).getPosition());
    }

    @Test
    void finalStandingsAllEqual() {
        adaPlayer.addPoints(5);
        adaPlayer.addKillShotTrackPoints(2);

        bedaPlayer.addPoints(5);
        bedaPlayer.addKillShotTrackPoints(2);

        cosoPlayer.addPoints(5);
        cosoPlayer.addKillShotTrackPoints(2);

        debianPlayer.addPoints(5);
        debianPlayer.addKillShotTrackPoints(2);

        // I expect ada, beda, debian, coso first
        List<StandingsItem> standings = controller.getFinalStandings();

        assertEquals(1, standings.get(0).getPosition());
        assertEquals(1, standings.get(1).getPosition());
        assertEquals(1, standings.get(2).getPosition());
        assertEquals(1, standings.get(3).getPosition());
    }
}