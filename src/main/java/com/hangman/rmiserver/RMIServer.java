package com.hangman.rmiserver;

import com.hangman.jndi.JNDIHelper;
import com.hangman.jndi.JNDIConfigurationKey;

import javax.naming.NamingException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer {
    private static final int RANDOM_PORT_HINT = 0;
    private static final int RMI_PORT = 1112;

    public static void main(String[] args) throws RemoteException{
        try {
            //   String rmiPortString = JNDIHelper.getConfigurationParameter(JNDIConfigurationKey.RMI_PORT_KEY);
            //   int rmiPort = Integer.parseInt(rmiPortString);

            // String rndPortString = JNDIHelper.getConfigurationParameter(JNDIConfigurationKey.RANDOM_PORT);
            //  int rndPort = Integer.parseInt(rndPortString);


            //REGISTRY => we register services that we want to be public to client applications
            //in Game Contrller => initialize method

            Registry registry = LocateRegistry.createRegistry(RMI_PORT);

            //ChatService => interface that enables us to do everything the server will do,
            // that is, which services exist on the interface, i.e. on the server
            ChatService chatService = new ChatServiceImpl();
            ChatService skeleton = (ChatService) UnicastRemoteObject.exportObject(chatService, RANDOM_PORT_HINT);
            registry.rebind(ChatService.REMOTE_OBJECT_NAME,  skeleton);
            System.err.println("Object registered in RMI registry");
        } catch ( IOException e ) {
            e.printStackTrace();
        }


    }
}