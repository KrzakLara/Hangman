package com.hangman;

import java.io.Serializable;

public class LoginMessage implements Serializable {

    private static final long serialVersionUID = 4L;

    private String playerId;

    private String username;

    public LoginMessage() {

    }

    public LoginMessage(String playerId, String username) {
        this.playerId = playerId;
        this.username = username;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
