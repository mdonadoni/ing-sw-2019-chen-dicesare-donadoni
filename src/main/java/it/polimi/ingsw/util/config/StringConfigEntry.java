package it.polimi.ingsw.util.config;
/**
 * Implements ConfigEntry as a string value
 */
public class StringConfigEntry implements ConfigEntry {
    /**
     * Name of the value
     */
    private String name;
    /**
     * String value.
     */
    private String value;

    /**
     * Constructor
     * @param name Name of the value.
     * @param value Value.
     */
    StringConfigEntry(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void parseString(String s) {
        value = s;
    }

    @Override
    public void set(String value) {
        this.value = value;
    }

    @Override
    public void set(int value) {
        throw new ConfigException("Called integer setter on string entry " + name);
    }

    @Override
    public String asString() {
        return value;
    }

    @Override
    public int asInt() {
        throw new ConfigException("Called asInt ont integer entry " + name);
    }
}
