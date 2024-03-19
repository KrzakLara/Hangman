package com.hangman.rmiserver;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ChatServiceImpl implements ChatService {

    private List<String> allMessages;
    public ChatServiceImpl() throws RemoteException {
        allMessages = new ArrayList<>();
    }
    @Override
    public void sendMessage(String newMessage, String userName) throws RemoteException {

        String userMsg = userName + " -> " + newMessage + "\n";
        allMessages.add(userMsg);
    }

    @Override
    public List<String> receiveAllMessages() {
        return allMessages;
    }
}
