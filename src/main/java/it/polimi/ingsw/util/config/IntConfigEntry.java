package it.polimi.ingsw.util.config;

public class IntConfigEntry implements ConfigEntry {
    private String name;
    private int value;

    IntConfigEntry(String name) {
        this.name = name;
    }

    IntConfigEntry(String name, int value) {
        this(name);
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setValue(String s) {
        try {
            value = Integer.parseInt(s);
        } catch (Exception e) {
            throw new ConfigException("Error while parsing " + name + ", got " + value, e);
        }
    }

    public void set(int value) {
        this.value = value;
    }

    public int get() {
        return  value;
    }
}
