package it.polimi.ingsw.util.config;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.util.Json;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private static Map<String, ConfigEntry> entriesMap = new HashMap<>();

    private static final IntConfigEntry ROUND_TIMEOUT = register(new IntConfigEntry("roundTimeout", 60));
    private static final IntConfigEntry LOBBY_TIMEOUT = register(new IntConfigEntry("lobbyTimeout", 30));
    private static final IntConfigEntry MIN_PLAYERS = register(new IntConfigEntry("minPlayers", 3));
    private static final IntConfigEntry MAX_PLAYERS = register(new IntConfigEntry("maxPlayers", 5));
    private static final StringConfigEntry HOSTNAME = register(new StringConfigEntry("hostname", "localhost"));

    private static <T extends ConfigEntry> T register(T property) {
        entriesMap.put(property.getName(), property);
        return property;
    }

    public static int getRoundTimeout() {
        return ROUND_TIMEOUT.get();
    }

    public static int getLobbyTimeout() {
        return LOBBY_TIMEOUT.get();
    }

    public static int getMinPlayers() {
        return MIN_PLAYERS.get();
    }

    public static int getMaxPlayers() {
        return MAX_PLAYERS.get();
    }

    public static String getHostname() {
        return HOSTNAME.get();
    }

    public static void setHostname(String hostname) {
        HOSTNAME.setValue(hostname);
    }

    public static void setLobbyTimeout(String timeout) {
        LOBBY_TIMEOUT.setValue(timeout);
    }

    public static void setRoundTimeout(String timeout) {
        ROUND_TIMEOUT.setValue(timeout);
    }

    private static void setEntry(String name, String value) {
        if(!entriesMap.containsKey(name)) {
            throw new ConfigException("Key not valid: " + name);
        }
        entriesMap.get(name).setValue(value);
    }

    public static void loadJson(InputStream stream) {
        try {
            ObjectMapper mapper = Json.getMapper();
            JsonNode jsonConfig = mapper.readTree(stream);
            jsonConfig.fields().forEachRemaining(entry -> {
                setEntry(entry.getKey(), entry.getValue().asText());
            });
        } catch (ConfigException e) {
            throw e;
        } catch (Exception e) {
            throw new ConfigException("Error while loading json", e);
        }
    }

}
