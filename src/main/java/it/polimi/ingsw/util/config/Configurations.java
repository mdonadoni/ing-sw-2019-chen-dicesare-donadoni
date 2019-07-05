package it.polimi.ingsw.util.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.util.Json;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used for the configuration
 */
public class Configurations {
    /**
     * Map of entry
     */
    private Map<String, ConfigEntry> entryMap = new HashMap<>();

    /**
     * Class int builder
     */
    public class IntBuilder {
        /**
         * Name of the value.
         */
        private String name;
        /**
         * The value.
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
         * Constructor of the class.
         * @param name The name of the value.
         */
        IntBuilder(String name) {
            this.name = name;
        }

        /**
         * Get the int builder with value.
         * @param value The value to set.
         * @return The int builder.
         */
        public IntBuilder withValue(int value) {
            this.value = value;
            return this;
        }
        /**
         * Get the int builder with minimum value.
         * @param minValue The minimum value to set.
         * @return The int builder.
         */
        public IntBuilder withMinValue(int minValue) {
            this.minValue = minValue;
            return this;
        }
        /**
         * Get the int builder with maximum value.
         * @param maxValue The maximum value to set.
         * @return The int builder.
         */
        public IntBuilder withMaxValue(int maxValue) {
            this.maxValue = maxValue;
            return this;
        }

        /**
         * Add an IntConfigEntry.
         */
        public void add() {
            Configurations.this.add(name, value, minValue, maxValue);
        }
    }

    /**
     * Add a ConfigEntry.
     * @param entry The ConfigEntru to add.
     */
    private void add(ConfigEntry entry) {
        if (entryMap.containsKey(entry.getName())) {
            throw new ConfigException("Key " + entry.getName() + " already inside");
        }
        entryMap.put(entry.getName(), entry);
    }

    /**
     * Throw if it doesn't find an entry.
     * @param name The entry name to find.
     */
    private void throwIfNotFound(String name) {
        if (!entryMap.containsKey(name)) {
            throw new ConfigException("Entry not found: " + name);
        }
    }

    /**
     * Add config entry with given name and integer value.
     * @param name Name of the entry.
     * @param value Integer value.
     */
    public void add(String name, int value) {
        add(new IntConfigEntry(name, value));
    }

    /**
     * Add config entry with given name and integer value.
     * @param name Name of the entry.
     * @param value Integer value.
     * @param minValue Minimum value of the entry.
     * @param maxValue Maximum value of the entry.
     */
    public void add(String name, int value, Integer minValue, Integer maxValue) {
        add(new IntConfigEntry(name, value, minValue, maxValue));
    }

    /**
     * Add config entry with given name and string value.
     * @param name Name of config entry.
     * @param value String value of config entry.
     */
    public void add(String name, String value) {
        add(new StringConfigEntry(name, value));
    }

    /**
     * Get string value from given config entry name.
     * @param name Name of the config entry.
     * @return String value of config entry.
     */
    public String getString(String name) {
        throwIfNotFound(name);
        return entryMap.get(name).asString();
    }

    /**
     * Get integer value from given config entry name.
     * @param name Name of config entry.
     * @return Integer value of config entry.
     */
    public int getInt(String name) {
        throwIfNotFound(name);
        return entryMap.get(name).asInt();
    }

    /**
     * Parse value of a config entry from a string. Useful to parse CLI arguments.
     * @param name
     * @param value
     */
    public void parseString(String name, String value) {
        throwIfNotFound(name);
        entryMap.get(name).parseString(value);
    }

    /**
     * Set value of string config entry.
     * @param name Name of config entry.
     * @param value Value to be set.
     */
    public void set(String name, String value) {
        throwIfNotFound(name);
        entryMap.get(name).set(value);
    }

    /**
     * Set value of integer config entry.
     * @param name Name of config entry.
     * @param value Integer value of config entry.
     */
    public void set(String name, int value) {
        throwIfNotFound(name);
        entryMap.get(name).set(value);
    }

    /**
     * Return the Builder to build an integer config entry.
     * @param name Name of config entry to be created.
     * @return Integer config entry builder.
     */
    public IntBuilder intBuilder(String name) {
        return new IntBuilder(name);
    }

    /**
     * Load from json.
     * @param stream The stream of input.
     */
    public void loadJson(InputStream stream) {
        try {
            ObjectMapper mapper = Json.getMapper();
            JsonNode jsonConfig = mapper.readTree(stream);
            jsonConfig.fields().forEachRemaining(entry -> parseString(entry.getKey(), entry.getValue().asText()));
        } catch (ConfigException e) {
            throw e;
        } catch (Exception e) {
            throw new ConfigException("Error while loading json", e);
        }
    }
}
