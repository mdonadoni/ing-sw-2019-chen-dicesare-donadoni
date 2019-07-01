package it.polimi.ingsw.util.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.util.Json;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Configurations {
    private Map<String, ConfigEntry> entryMap = new HashMap<>();

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
