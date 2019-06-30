package it.polimi.ingsw.util.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigTest {
    @Test
    void config() {
        assertEquals(Config.getLobbyTimeout(), 30);
    }

    @Test
    void loadJson() {
        Config.loadJson(getClass().getResourceAsStream("/configtest.json"));
        assertEquals(Config.getMinPlayers(), 3);
        assertEquals(Config.getHostname(), "10.0.0.1");
    }
}