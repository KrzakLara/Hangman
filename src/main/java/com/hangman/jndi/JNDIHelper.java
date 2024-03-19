package com.hangman.jndi;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


//JNDI => a technology that creates a context in which some common resources are registered
public class JNDIHelper {

    private static final String PROVIDER_URL = "file:C:/temp";

    private static final String CONFIGURATION_FILE_NAME = "jndi.properties";

    private static InitialContext context;

    //Creating a context that is loaded in a file
    private static InitialContext getInitialContext() throws NamingException {
        if(context == null) {
            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
            props.setProperty(Context.PROVIDER_URL, PROVIDER_URL);
            context = new InitialContext(props);
        }

        return context;
    }

    //we retrieve the resource and read everything that is in the file
    public static String getConfigurationParameter(JNDIConfigurationKey param) throws NamingException, IOException {
        Object configurationFileName = getInitialContext().lookup(CONFIGURATION_FILE_NAME);
        Properties configurationProperties = new Properties();
        configurationProperties.load(new FileReader(configurationFileName.toString()));
        return configurationProperties.getProperty(param.getKey());
    }
}
