package it.polimi.ingsw.util.config;

public class IntConfigEntry implements ConfigEntry {
    private String name;
    private int value;
    private Integer minValue = null;
    private Integer maxValue = null;

    IntConfigEntry(String name, int value) {
        this.name = name;
        set(value);
    }

    IntConfigEntry(String name, int value, Integer minValue, Integer maxValue) {
        this.name = name;
        this.minValue = minValue;
        this.maxValue = maxValue;
        set(value);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void parseString(String s) {
        try {
            set(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            throw new ConfigException("Error while parsing " + name + ", got " + value, e);
        }
    }

    @Override
    public void set(String value) {
        throw new ConfigException("Called string setter on integer config " + name);
    }

    @Override
    public void set(int value) {
        if (minValue != null && value < minValue) {
            throw new ConfigException("Cannot set " + name + " to less than " + minValue);
        }if (maxValue != null && value > maxValue) {
            throw new ConfigException("Cannot set " + name + " to more than " + maxValue);
        }
        this.value = value;
    }

    @Override
    public String asString() {
        throw new ConfigException("Called asString on integer config " + name);
    }

    @Override
    public int asInt() {
        return value;
    }
}
