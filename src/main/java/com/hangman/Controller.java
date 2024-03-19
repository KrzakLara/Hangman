package com.hangman;


import com.hangman.data.GameMetaData;
import com.hangman.server.Server;
import com.hangman.thread.ClientThread;
import hr.algebra.hangman.model.ClientData;
import hr.algebra.hangman.model.PlayerDetails;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller implements Initializable {

    @FXML
    private TextField tfPlayerName;

    private static PlayerDetails playerDetails;
    private static ClientData firstClientData;
    private static ClientData secondClientData;
    private String playerId;

    private Integer playerIdInt=0;
    private static String playerName;

    private Boolean playerOneTurn;

    public void start() throws IOException {

        //connect to server

        try (Socket clientSocket = new Socket(Server.HOST, Server.PORT))
        {
            System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" +clientSocket.getPort());

           // sendSerializableRequest(clientSocket);
            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
            playerName = tfPlayerName.getText();
            playerIdInt++;
            playerId = playerIdInt.toString();
            oos.writeObject(new LoginMessage(playerId, tfPlayerName.getText()));
            System.out.println("Client sent message back to the server!");

        }
         catch (IOException e)
        {
        e.printStackTrace();
        }


        String  playerName = tfPlayerName.getText();

        String playerOneName = playerName;


        playerDetails = new PlayerDetails(playerOneName);
        if (playerOneName=="" ) return;

        System.out.println("Welcome " + playerName + "!");

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("insert-word.fxml"));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 400);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = Main.getMainStage();

        stage.setTitle("Hangman game!");
        stage.setScene(scene);
        stage.show();

    }

    public static String getPlayerName() {
        return playerName;
    }

    public static PlayerDetails getPlayerDetails()
    {
        return playerDetails;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }



    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    private static void sendSerializableRequest(Socket client) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());

        ClientData clientDataFromServer = (ClientData) ois.readObject();

        System.out.println("Information received: " + clientDataFromServer.getPort());

        if(firstClientData == null) {
            firstClientData = clientDataFromServer;
        }
        else {
            secondClientData = clientDataFromServer;
        }
    }


}