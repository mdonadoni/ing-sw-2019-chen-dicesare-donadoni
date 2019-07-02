package it.polimi.ingsw.common;

import it.polimi.ingsw.model.PlayerToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class StandingsItemTest {
    StandingsItem item;

    @BeforeEach
    void setUp() {
        item = new StandingsItem(1, "bello", 123, PlayerToken.GREEN);
    }

    @Test
    void jackson() throws IOException {
        StandingsItem des = UtilSerialization.jackson(item, StandingsItem.class);
        assertEquals(item.getColor(), des.getColor());
        assertEquals(item.getNickname(), des.getNickname());
        assertEquals(item.getPoints(), des.getPoints());
        assertEquals(item.getPosition(), des.getPosition());
    }

    @Test
    void javaSerializable() throws IOException, ClassNotFoundException {
        StandingsItem des = UtilSerialization.javaSerializable(item, StandingsItem.class);
        assertEquals(item.getColor(), des.getColor());
        assertEquals(item.getNickname(), des.getNickname());
        assertEquals(item.getPoints(), des.getPoints());
        assertEquals(item.getPosition(), des.getPosition());
    }
}