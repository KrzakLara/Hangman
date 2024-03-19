package hr.algebra.hangman.model;

import java.io.Serializable;

public class PlayerDetails implements Serializable {

    private String playerName;

    private Integer port;

    private String ipAddress;

    public PlayerDetails(String ipAddress) {

        this.ipAddress = ipAddress;
    }

    public Integer getPort() {
        return port;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
