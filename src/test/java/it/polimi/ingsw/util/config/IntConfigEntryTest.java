package it.polimi.ingsw.util.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntConfigEntryTest {
    @Test
    void minValue() {
        IntConfigEntry entry = new IntConfigEntry("a", 1, 1, null);
        assertEquals(1, entry.asInt());
        entry.set(2);
        assertEquals(2, entry.asInt());
    }

    @Test
    void minValueThrows() {
        IntConfigEntry entry = new IntConfigEntry("a", 1, 1, null);
        assertThrows(ConfigException.class, () -> entry.set(0));
    }

    @Test
    void maxValue() {
        IntConfigEntry entry = new IntConfigEntry("a", 1, null, 2);
        assertEquals(1, entry.asInt());
        entry.set(2);
        assertEquals(2, entry.asInt());
    }

    @Test
    void maxValueThrows() {
        IntConfigEntry entry = new IntConfigEntry("a", 1, null, 2);
        assertThrows(ConfigException.class, () -> entry.set(3));
    }


}