package it.polimi.ingsw.util.config;


import java.io.InputStream;

public class Config {
    private static Configurations config = null;

    private static final String TURN_TIMEOUT = "turnTimeout";
    private static final String LOBBY_TIMEOUT = "lobbyTimeout";
    private static final String MIN_PLAYERS = "minPlayers";
    private static final String MAX_PLAYERS = "maxPlayers";
    private static final String HOSTNAME = "hostname";
    private static final String SKULLS = "skulls";


    private static void init() {
        if (config == null) {
            config = new Configurations();
            config.add(TURN_TIMEOUT, 60);
            config.add(LOBBY_TIMEOUT, 30);
            config.add(MIN_PLAYERS, 3);
            config.add(MAX_PLAYERS, 5);
            config.add(HOSTNAME, "localhost");
            config.add(SKULLS, 8);
        }
    }

    public static int getTurnTimeout() {
        init();
        return config.getInt(TURN_TIMEOUT);
    }

    public static int getLobbyTimeout() {
        init();
        return config.getInt(LOBBY_TIMEOUT);
    }

    public static int getMinPlayers() {
        init();
        return config.getInt(MIN_PLAYERS);
    }

    public static int getMaxPlayers() {
        init();
        return config.getInt(MAX_PLAYERS);
    }

    public static String getHostname() {
        init();
        return config.getString(HOSTNAME);
    }

    public static int getSkulls() {
        init();
        return config.getInt(SKULLS);
    }

    public static void parseHostname(String hostname) {
        init();
        config.parseString(HOSTNAME, hostname);
    }

    public static void parseLobbyTimeout(String timeout) {
        init();
        config.parseString(LOBBY_TIMEOUT, timeout);
    }

    public static void parseTurnTimeout(String timeout) {
        init();
        config.parseString(TURN_TIMEOUT, timeout);
    }

    public static void parseSkulls(String skulls) {
        init();
        config.parseString(SKULLS, skulls);
    }

    public static void loadJson(InputStream stream) {
        init();
        config.loadJson(stream);
    }



}
