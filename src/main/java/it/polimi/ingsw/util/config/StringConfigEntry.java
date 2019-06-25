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
    public void setValue(String s) {
        value = s;
    }

    public String get() {
        return value;
    }
}
