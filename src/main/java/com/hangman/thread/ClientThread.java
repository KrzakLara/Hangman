package com.hangman.thread;

import com.hangman.data.GameMetaData;
import com.hangman.data.PlayerMetaData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

import static com.hangman.server.Server.PORT;

public class ClientThread  implements Runnable {

    //when the client knows everything he needs, he starts a thread and the thread listens
    private GameMetaData gameMetaData;

    public ClientThread(GameMetaData gameMetaData) {
        this.gameMetaData = gameMetaData;
    }

    @Override
    public void run() {


        try (ServerSocket serverSocket = new ServerSocket(2022)) {
            System.err.println("Client #1 listening on port: " + serverSocket.getLocalPort());

            while (true) {

                System.out.println("Client thread started and waiting for server response!");

                Socket clientSocket = serverSocket.accept();

                System.out.println("Server message accepted!");

                System.out.println("Server message accepted at " + LocalDateTime.now());

                try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                     ObjectOutputStream oos = new ObjectOutputStream((clientSocket.getOutputStream()))) {
                    GameMetaData gameMetaData1 = (GameMetaData) ois.readObject();
                    System.out.println("Received new game meta data  with the name: " + gameMetaData1.getPlayerOneData());
                    System.out.println("New game state loaded!");
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}