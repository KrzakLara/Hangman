package com.hangman.data;

import hr.algebra.hangman.model.PlayerDetails;

import java.io.Serializable;

public class GameMetaData implements Serializable {

    private PlayerMetaData playerOneData;
    private PlayerDetails playerTwoData;

    public GameMetaData() {

    }

    public GameMetaData(PlayerMetaData playerOneData, PlayerDetails playerTwoData) {
        this.playerOneData = playerOneData;
        this.playerTwoData = playerTwoData;
    }

    public PlayerMetaData getPlayerOneData() {
        return playerOneData;
    }

    public void setPlayerOneData(PlayerMetaData playerOneData) {
        this.playerOneData = playerOneData;
    }

    public PlayerDetails getPlayerTwoData() {
        return playerTwoData;
    }

    public void setPlayerTwoData(PlayerDetails playerTwoData) {
        this.playerTwoData = playerTwoData;
    }
}
