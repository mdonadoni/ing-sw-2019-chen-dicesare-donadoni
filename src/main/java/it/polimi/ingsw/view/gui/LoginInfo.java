package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.ConnectionType;

/**
 * Contains the information of the login
 */
public class LoginInfo {
    /**
     * The type of connection
     */
    private ConnectionType type;
    /**
     * Address of the client
     */
    private String address;
    /**
     * Used port of the connection
     */
    private int port;
    /**
     * Nickname of the player
     */
    private String nickname;

    /**
     * Constructor
     * @param address Address of the client
     * @param port port of the connection
     * @param type type of connection
     * @param nickname nickname of the player
     */
    public LoginInfo(String address, int port, ConnectionType type, String nickname) {
        this.address = address;
        this.port = port;
        this.type = type;
        this.nickname = nickname;
    }

    public ConnectionType getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getNickname() {
        return nickname;
    }
}
