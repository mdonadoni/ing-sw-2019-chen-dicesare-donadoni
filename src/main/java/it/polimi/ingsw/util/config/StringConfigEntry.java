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

    /**
     * Get name of this config entry.
     * @return Name of this config entry.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Parse the value from a String. Since this is a String config entry,
     * the String is simply copied.
     * @param s String to be parsed.
     */
    @Override
    public void parseString(String s) {
        value = s;
    }

    /**
     * Set String value of this config entry.
     * @param value String value.
     */
    @Override
    public void set(String value) {
        this.value = value;
    }

    /**
     * Throw since this is a String config entry.
     * @param value Integer value.
     */
    @Override
    public void set(int value) {
        throw new ConfigException("Called integer setter on string entry " + name);
    }

    /**
     * Return the value of this config entry.
     * @return Value of this config entry.
     */
    @Override
    public String asString() {
        return value;
    }

    /**
     * Throw since this a String config entry.
     * @return Integer value.
     */
    @Override
    public int asInt() {
        throw new ConfigException("Called asInt ont integer entry " + name);
    }
}
