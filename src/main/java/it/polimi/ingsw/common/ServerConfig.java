package it.polimi.ingsw.common;


import it.polimi.ingsw.util.config.Configurations;

import java.io.InputStream;

/**
 * This class is used to configure the server.
 */
public class ServerConfig {
    /**
     * This object maintains and validates the configs of the server.
     */
    private static Configurations config = null;
    /**
     * Timeout of the turn.
     */
    private static final String TURN_TIMEOUT = "turnTimeout";
    /**
     * Timeout of the lobby before starting the match after 3 players are inside the lobby.
     */
    private static final String LOBBY_TIMEOUT = "lobbyTimeout";
    /**
     * Timeout of single actions (respawn and tagback granade).
     */
    private static final String SINGLE_ACTION_TIMEOUT = "singleActionTimeout";
    /**
     * Minimum number of players.
     */
    private static final String MIN_PLAYERS = "minPlayers";
    /**
     * Maximum number of players.
     */
    private static final String MAX_PLAYERS = "maxPlayers";
    /**
     * Hostname of the server.
     */
    private static final String HOSTNAME = "hostname";
    /**
     * Number of skulls in a match.
     */
    private static final String SKULLS = "skulls";
    /**
     * RMI port of the registry.
     */
    private static final String RMI_PORT = "rmiPort";
    /**
     * Port on which the server will listen for socket connections.
     */
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
     * -timeout of single actions as 60 and minimum 1
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

            config.intBuilder(SINGLE_ACTION_TIMEOUT)
                    .withValue(60)
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

    /**
     * Get the turn timeout.
     * @return Turn timeout.
     */
    public static synchronized int getTurnTimeout() {
        return config.getInt(TURN_TIMEOUT);
    }
    /**
     * Get the lobby timeout.
     * @return Lobby timeout.
     */
    public static synchronized int getLobbyTimeout() {
        return config.getInt(LOBBY_TIMEOUT);
    }
    /**
     * Get the single action timeout.
     * @return Single action timeout.
     */
    public static synchronized int getSingleActionTimeout() {
        return config.getInt(SINGLE_ACTION_TIMEOUT);
    }
    /**
     * Get the minimum number of players.
     * @return Minimum number of players.
     */
    public static synchronized int getMinPlayers() {
        return config.getInt(MIN_PLAYERS);
    }
    /**
     * Get the maximum number of players.
     * @return Maximum number of players.
     */
    public static synchronized int getMaxPlayers() {
        return config.getInt(MAX_PLAYERS);
    }
    /**
     * Get the hostname.
     * @return Hostname.
     */
    public static synchronized String getHostname() {
        return config.getString(HOSTNAME);
    }
    /**
     * Get the number of skulls.
     * @return Number of skulls.
     */
    public static synchronized int getSkulls() {
        return config.getInt(SKULLS);
    }
    /**
     * Get the port of RMI registry.
     * @return RMI registry port.
     */
    public static synchronized int getRmiPort() {
        return config.getInt(RMI_PORT);
    }
    /**
     * Get the port for socket connections.
     * @return Port for socket connections.
     */
    public static synchronized int getSocketPort() {
        return config.getInt(SOCKET_PORT);
    }

    /**
     * Parse the hostname from string. Useful for parsing CLI arguments.
     * @param hostname String to be parsed.
     */
    public static synchronized void parseHostname(String hostname) {
        config.parseString(HOSTNAME, hostname);
    }
    /**
     * Parse the lobby timeout from string. Useful for parsing CLI arguments.
     * @param timeout String to be parsed.
     */
    public static synchronized void parseLobbyTimeout(String timeout) {
        config.parseString(LOBBY_TIMEOUT, timeout);
    }
    /**
     * Parse the turn timeout from string. Useful for parsing CLI arguments.
     * @param timeout String to be parsed.
     */
    public static synchronized void parseTurnTimeout(String timeout) {
        config.parseString(TURN_TIMEOUT, timeout);
    }
    /**
     * Parse the single action timeout from string. Useful for parsing CLI arguments.
     * @param timeout String to be parsed.
     */
    public static synchronized void parseSingleActionTimeout(String timeout) {
        config.parseString(SINGLE_ACTION_TIMEOUT, timeout);
    }
    /**
     * Parse the skulls from string. Useful for parsing CLI arguments.
     * @param skulls String to be parsed.
     */
    public static synchronized void parseSkulls(String skulls) {
        config.parseString(SKULLS, skulls);
    }
    /**
     * Parse the rmi port from string. Useful for parsing CLI arguments.
     * @param rmiPort String to be parsed.
     */
    public static synchronized void parseRmiPort(String rmiPort) {
        config.parseString(RMI_PORT, rmiPort);
    }
    /**
     * Parse the socket port from string. Useful for parsing CLI arguments.
     * @param socketPort String to be parsed.
     */
    public static synchronized void parseSocketPort(String socketPort) {
        config.parseString(SOCKET_PORT, socketPort);
    }
    /**
     * Load config from json.
     * @param stream Stream to read.
     */
    public static synchronized void loadJson(InputStream stream) {
        config.loadJson(stream);
    }
}
