package it.polimi.ingsw.util.config;

/**
 * Implements ConfigEntry as a int value
 */
public class IntConfigEntry implements ConfigEntry {
    /**
     * Name of the value.
     */
    private String name;
    /**
     * Value of the int.
     */
    private int value;
    /**
     * Minimum of the value.
     */
    private Integer minValue = null;
    /**
     * Maximum of the value.
     */
    private Integer maxValue = null;

    /**
     * Constructor of the class
     * @param name Name of the value.
     * @param value Value.
     */
    IntConfigEntry(String name, int value) {
        this.name = name;
        set(value);
    }

    /**
     * Constructor of the class
     * @param name Name of the value.
     * @param value Value.
     * @param minValue Minimum of the value.
     * @param maxValue Maximum of the value.
     */
    IntConfigEntry(String name, int value, Integer minValue, Integer maxValue) {
        this.name = name;
        this.minValue = minValue;
        this.maxValue = maxValue;
        set(value);
    }

    /**
     * Get name of this config entry.
     * @return Name of config entry.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Set a value from a string input.
     * @param s String input to parse as int value.
     */
    @Override
    public void parseString(String s) {
        try {
            set(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            throw new ConfigException("Error while parsing " + name + ", got " + value, e);
        }
    }

    /**
     * Throw exception since this is an integer config.
     * @param value The given string,
     */
    @Override
    public void set(String value) {
        throw new ConfigException("Called string setter on integer config " + name);
    }

    /**
     * Set the value that must be between minValue and maxValue.
     * @param value The new value.
     */
    @Override
    public void set(int value) {
        if (minValue != null && value < minValue) {
            throw new ConfigException("Cannot set " + name + " to less than " + minValue);
        }
        if (maxValue != null && value > maxValue) {
            throw new ConfigException("Cannot set " + name + " to more than " + maxValue);
        }
        this.value = value;
    }

    /**
     * Throw since this is a integer config entry.
     * @return String value.
     */
    @Override
    public String asString() {
        throw new ConfigException("Called asString on integer config " + name);
    }

    /**
     * Return the value of this integer config entry.
     * @return Integer value of this config entry.
     */
    @Override
    public int asInt() {
        return value;
    }
}
