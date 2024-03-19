package com.hangman.enumeration;

public enum StatusLetter {

    RIGHTGUESSEDLETTER(0), WRONGGUESSEDLETTER(1);

    private final int status;


    StatusLetter(int status)
    {
        this.status = status;
    }

    public int getStatus()
    {
        return this.status;
    }


}
