package it.polimi.ingsw.util.config;

public class IntConfigEntry implements ConfigEntry {
    private String name;
    private int value;

    IntConfigEntry(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void parseString(String s) {
        try {
            value = Integer.parseInt(s);
        } catch (Exception e) {
            throw new ConfigException("Error while parsing " + name + ", got " + value, e);
        }
    }

    @Override
    public void set(String value) {
        throw new ConfigException("Called string setter on integer config " + name);
    }

    @Override
    public void set(int value) {
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
