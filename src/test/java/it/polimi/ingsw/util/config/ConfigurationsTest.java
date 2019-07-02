package it.polimi.ingsw.util.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigurationsTest {

    Configurations config;

    @BeforeEach
    void setUp() {
        config = new Configurations();
        config.add("prova1", 12);
        config.add("prova2", "asd");
    }

    @Test
    void getString() {
        assertEquals("asd", config.getString("prova2"));
    }

    @Test
    void getInt() {
        assertEquals(12, config.getInt("prova1"));
    }

    @Test
    void getIntThrow() {
        assertThrows(ConfigException.class, () -> config.getInt("prova2"));
    }

    @Test
    void getStringThrow() {
        assertThrows(ConfigException.class, () -> config.getString("prova1"));
    }

    @Test
    void setString() {
        config.set("prova2", "clazz");
        assertEquals("clazz", config.getString("prova2"));
    }

    @Test
    void setInt() {
        config.set("prova1", 10);
        assertEquals(10, config.getInt("prova1"));
    }

    @Test
    void setIntThrow() {
        assertThrows(ConfigException.class, () -> config.set("prova2", 15));
    }

    @Test
    void setStringThrow() {
        assertThrows(ConfigException.class, () -> config.set("prova1", "asd"));
    }

    @Test
    void loadJson() {
        config.loadJson(getClass().getResourceAsStream("/configtest.json"));
        assertEquals(config.getInt("prova1"), 25);
        assertEquals(config.getString("prova2"), "lollipop");
    }

    @Test
    void builder() {
        config.intBuilder("builder").withValue(2).withMinValue(2).withMaxValue(3).add();
        assertEquals(2, config.getInt("builder"));
        assertThrows(ConfigException.class, () -> config.set("builder", 1));
    }
}