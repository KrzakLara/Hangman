package com.hangman;

import com.hangman.data.GameMetaData;
import com.hangman.data.PlayerMetaData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static com.hangman.server.Server.PORT;

public class Main extends Application {
    private static Stage mainStage;
    private static String playerName = "";

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        //to override properties our scene builder app
        Scene scene = new Scene((Parent) fxmlLoader.load(), 600, 400);
        stage.setTitle("Hangman game!");
        stage.setScene(scene);
        stage.show();
        mainStage = stage;

}

    public static String getPlayerName() {
        return playerName;
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void main(String[] args) {
        launch();
    }
}