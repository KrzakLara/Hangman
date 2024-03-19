package com.hangman.jndi;

public enum JNDIConfigurationKey {

    RMI_PORT_KEY("rmi.port"),

    RANDOM_PORT("random.port");

    //BACKEND_SERVER_PORT("backend.server");

    private String key;

    private JNDIConfigurationKey(String key)
    {
        this.key = key;
    }

    public String getKey()
    {
        return key;
    }
}
