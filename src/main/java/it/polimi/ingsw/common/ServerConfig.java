package it.polimi.ingsw.common;


import it.polimi.ingsw.util.config.Configurations;

import java.io.InputStream;

/**
 * This class is used to configure the server.
 */
public class ServerConfig {
    private static Configurations config = null;

    private static final String TURN_TIMEOUT = "turnTimeout";
    private static final String LOBBY_TIMEOUT = "lobbyTimeout";
    private static final String MIN_PLAYERS = "minPlayers";
    private static final String MAX_PLAYERS = "maxPlayers";
    private static final String HOSTNAME = "hostname";
    private static final String SKULLS = "skulls";
    private static final String RMI_PORT = "rmiPort";
    private static final String SOCKET_PORT = "socketPort";

    static {
        init();
    }

    /**
     * This class should not be constructed.
     */
    private ServerConfig() { }

    /**
     * Initializer method:
     * -timeout of the turn as 60 and minimum 1
     * -timeout of the lobby as 30 and minimum 1
     * -minimum players as 3
     * -maximum players as 5
     * -hostname as localhost
     * -RMI port as 1099 and the minimum as 1
     * -socket port as 9999 and the minimum as 1
     */
    private static synchronized void init() {
        if (config == null) {
            config = new Configurations();

            config.intBuilder(TURN_TIMEOUT)
                    .withValue(60)
                    .withMinValue(1)
                    .add();

            config.intBuilder(LOBBY_TIMEOUT)
                    .withValue(30)
                    .withMinValue(1)
                    .add();

            config.intBuilder(MIN_PLAYERS)
                    .withValue(3)
                    .withMinValue(3)
                    .withMaxValue(3)
                    .add();

            config.intBuilder(MAX_PLAYERS)
                    .withValue(5)
                    .withMinValue(5)
                    .withMaxValue(5)
                    .add();

            config.add(HOSTNAME, "localhost");

            config.intBuilder(SKULLS)
                    .withValue(8)
                    .withMinValue(1)
                    .withMaxValue(8)
                    .add();

            config.intBuilder(RMI_PORT)
                    .withValue(1099)
                    .withMinValue(1)
                    .add();

            config.intBuilder(SOCKET_PORT)
                    .withValue(9999)
                    .withMinValue(1)
                    .add();
        }
    }

    public static synchronized int getTurnTimeout() {
        return config.getInt(TURN_TIMEOUT);
    }

    public static synchronized int getLobbyTimeout() {
        return config.getInt(LOBBY_TIMEOUT);
    }

    public static synchronized int getMinPlayers() {
        return config.getInt(MIN_PLAYERS);
    }

    public static synchronized int getMaxPlayers() {
        return config.getInt(MAX_PLAYERS);
    }

    public static synchronized String getHostname() {
        return config.getString(HOSTNAME);
    }

    public static synchronized int getSkulls() {
        return config.getInt(SKULLS);
    }

    public static synchronized int getRmiPort() {
        return config.getInt(RMI_PORT);
    }

    public static synchronized int getSocketPort() {
        return config.getInt(SOCKET_PORT);
    }

    public static synchronized void parseHostname(String hostname) {
        config.parseString(HOSTNAME, hostname);
    }

    public static synchronized void parseLobbyTimeout(String timeout) {
        config.parseString(LOBBY_TIMEOUT, timeout);
    }

    public static synchronized void parseTurnTimeout(String timeout) {
        config.parseString(TURN_TIMEOUT, timeout);
    }

    public static synchronized void parseSkulls(String skulls) {
        config.parseString(SKULLS, skulls);
    }

    public static synchronized void parseRmiPort(String rmiPort) {
        config.parseString(RMI_PORT, rmiPort);
    }

    public static synchronized void parseSocketPort(String socketPort) {
        config.parseString(SOCKET_PORT, socketPort);
    }

    public static synchronized void loadJson(InputStream stream) {
        config.loadJson(stream);
    }
}
