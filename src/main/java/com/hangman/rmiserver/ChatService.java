package com.hangman.rmiserver;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatService extends Remote  {

    String REMOTE_OBJECT_NAME = "com.hangman.rmi.service";

    void sendMessage(String newMessage, String userName) throws RemoteException;

    List<String> receiveAllMessages();
}
