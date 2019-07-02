package it.polimi.ingsw.util.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.util.Json;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Configurations {
    private Map<String, ConfigEntry> entryMap = new HashMap<>();

    public class IntBuilder {
        private String name;
        private int value;
        private Integer minValue = null;
        private Integer maxValue = null;

        IntBuilder(String name) {
            this.name = name;
        }

        public IntBuilder withValue(int value) {
            this.value = value;
            return this;
        }

        public IntBuilder withMinValue(int minValue) {
            this.minValue = minValue;
            return this;
        }

        public IntBuilder withMaxValue(int maxValue) {
            this.maxValue = maxValue;
            return this;
        }

        public void add() {
            Configurations.this.add(name, value, minValue, maxValue);
        }
    }

    private void add(ConfigEntry entry) {
        if (entryMap.containsKey(entry.getName())) {
            throw new ConfigException("Key " + entry.getName() + " already inside");
        }
        entryMap.put(entry.getName(), entry);
    }

    private void throwIfNotFound(String name) {
        if (!entryMap.containsKey(name)) {
            throw new ConfigException("Entry not found: " + name);
        }
    }

    public void add(String name, int value) {
        add(new IntConfigEntry(name, value));
    }

    public void add(String name, int value, Integer minValue, Integer maxValue) {
        add(new IntConfigEntry(name, value, minValue, maxValue));
    }


    public void add(String name, String value) {
        add(new StringConfigEntry(name, value));
    }

    public String getString(String name) {
        throwIfNotFound(name);
        return entryMap.get(name).asString();
    }

    public int getInt(String name) {
        throwIfNotFound(name);
        return entryMap.get(name).asInt();
    }

    public void parseString(String name, String value) {
        throwIfNotFound(name);
        entryMap.get(name).parseString(value);
    }

    public void set(String name, String value) {
        throwIfNotFound(name);
        entryMap.get(name).set(value);
    }

    public void set(String name, int value) {
        throwIfNotFound(name);
        entryMap.get(name).set(value);
    }

    public IntBuilder intBuilder(String name) {
        return new IntBuilder(name);
    }

    public void loadJson(InputStream stream) {
        try {
            ObjectMapper mapper = Json.getMapper();
            JsonNode jsonConfig = mapper.readTree(stream);
            jsonConfig.fields().forEachRemaining(entry -> {
                parseString(entry.getKey(), entry.getValue().asText());
            });
        } catch (ConfigException e) {
            throw e;
        } catch (Exception e) {
            throw new ConfigException("Error while loading json", e);
        }
    }
}
