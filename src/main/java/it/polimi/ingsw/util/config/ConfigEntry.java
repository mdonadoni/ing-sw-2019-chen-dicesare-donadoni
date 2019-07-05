package it.polimi.ingsw.util.config;

/**
 * Interface of ConfigEntry
 */
public interface ConfigEntry {
    /**
     * Get name of config entry.
     * @return Name of config entry.
     */
    String getName();

    /**
     * Parse the value of this config from a String representation. Useful to
     * parse from CLI arguments.
     * @param value String to be parsed.
     */
    void parseString(String value);

    /**
     * Set String value if applicable.
     * @param value String value.
     */
    void set(String value);

    /**
     * Set integer value if applicable.
     * @param value Integer value.
     */
    void set(int value);

    /**
     * Return the string value if applicable.
     * @return String value.
     */
    String asString();

    /**
     * Return the integer value if applicable.
     * @return Integer value.
     */
    int asInt();
}
