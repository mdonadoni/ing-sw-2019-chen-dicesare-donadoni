package it.polimi.ingsw.common;


import it.polimi.ingsw.util.config.Configurations;

import java.io.InputStream;

public class ClientConfig {
    private static Configurations config = null;

    private static final String HOSTNAME = "hostname";
    private static final String RMI_PORT = "rmiPort";
    private static final String SOCKET_PORT = "socketPort";

    static {
        init();
    }

    private static synchronized void init() {
        if (config == null) {
            config = new Configurations();

            config.add(HOSTNAME, "localhost");

            config.intBuilder(RMI_PORT)
                    .withValue(1099)
                    .withMinValue(1024)
                    .add();

            config.intBuilder(SOCKET_PORT)
                    .withValue(9999)
                    .withMinValue(1024)
                    .add();
        }
    }

    public static synchronized String getHostname() {
        return config.getString(HOSTNAME);
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
