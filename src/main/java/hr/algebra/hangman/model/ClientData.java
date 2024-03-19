package hr.algebra.hangman.model;

import java.io.Serializable;

public class ClientData implements Serializable {

    private Integer port;
    private String ipAddress;

    private Boolean serverConnected;

    public ClientData(Integer port, String ipAddress, Boolean serverConnected) {
        this.port = port;
        this.ipAddress = ipAddress;
        this.serverConnected = serverConnected;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Boolean isServerConnected() {
        return serverConnected;
    }

    public void setServerConnected(Boolean serverConnected) {
        this.serverConnected = serverConnected;
    }
}
