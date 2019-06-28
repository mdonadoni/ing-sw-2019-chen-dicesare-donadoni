package it.polimi.ingsw.controller;

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
        assertEquals(adaPlayer.getPoints(), 9);
        // Second damage dealer: 6 points
        assertEquals(cosoPlayer.getPoints(), 6);
        // Last one, but should also have a token on the kill track
        assertEquals(debianPlayer.getPoints(), 4);
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
        assertEquals(bedaPlayer.getPoints(), 7);
        assertEquals(bedaPlayer.getMarks().get(0), adaColor);

        // Then we have Debian with 4 points
        assertEquals(debianPlayer.getPoints(), 4);

        // Finally Coso with 2 points
        assertEquals(cosoPlayer.getPoints(), 2);
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
        assertEquals(debianPlayer.getPoints(), 2);
        assertEquals(cosoPlayer.getPoints(), 1);
        assertEquals(bedaPlayer.getPoints(), 1);
        assertEquals(adaPlayer.getPoints(), 0);
    }

    @Test
    void multikillTest(){
        bedaPlayer.addDamageWithoutMarks(adaColor, 11);
        cosoPlayer.addDamageWithoutMarks(adaColor, 11);

        controller.lookForScoreUpdates();

        // Ada should have 8 + 1 + 8 + 1 + 1
        assertEquals(adaPlayer.getPoints(), 19);
    }
}