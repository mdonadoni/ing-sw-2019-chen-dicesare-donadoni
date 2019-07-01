package it.polimi.ingsw.util.config;

public interface ConfigEntry {
    String getName();
    void parseString(String value);
    void set(String value);
    void set(int value);
    String asString();
    int asInt();
}
