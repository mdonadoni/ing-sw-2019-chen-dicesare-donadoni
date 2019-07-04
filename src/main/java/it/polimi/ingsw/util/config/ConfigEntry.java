package it.polimi.ingsw.util.config;

/**
 * Interface of ConfigEntry
 */
public interface ConfigEntry {
    String getName();
    void parseString(String value);
    void set(String value);
    void set(int value);
    String asString();
    int asInt();
}
