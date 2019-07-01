package it.polimi.ingsw.util.config;

public class StringConfigEntry implements ConfigEntry {
    private String name;
    private String value;

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
