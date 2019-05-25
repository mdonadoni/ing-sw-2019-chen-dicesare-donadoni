package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.ConnectionType;

public class LoginInfo {
    private ConnectionType type;
    private String address;
    private int port;
    private String nickname;

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
