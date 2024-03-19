package com.hangman.server;


import hr.algebra.hangman.model.ClientData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    public static final String HOST = "localhost";
    public static final int PORT = 2007;

    private static final Integer NUMBER_OF_PLAYERS = 2;

    private static Map<Integer, ClientData> clientsMap = new HashMap<Integer, ClientData>();



    public static void main(String[] args) {
        acceptRequests();
    }

    //the client listens to what the server sends
    private static void acceptRequests() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected from port: " + clientSocket.getPort());

                clientsMap.put(clientSocket.getPort(), new ClientData(clientSocket.getPort(), "127.0.0.1",
                        false));

                if(clientsMap.size() == 2) {
                    System.out.println("Entered");
                    for(Integer port : clientsMap.keySet()) {

                        System.out.println("Trying to connect on client port " + port);

                        try (Socket serverClientSocket = new Socket("localhost", port)) {
                            //System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

                            ObjectOutputStream oos = new ObjectOutputStream(serverClientSocket.getOutputStream());

                            System.out.println("Sending \"START_GAME\" message to client on port " + port);
                            oos.writeObject("START_GAME");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // outer try catch blocks cannot handle the anonymous implementations
                //new Thread(() ->  processPrimitiveClient(clientSocket)).start();
             //   new Thread(() ->  processSerializableClient(clientSocket)).start();
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void processSerializableClient(Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());){

            System.out.println("Request processed!");
            oos.writeObject(clientsMap.get(clientSocket.getPort()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
