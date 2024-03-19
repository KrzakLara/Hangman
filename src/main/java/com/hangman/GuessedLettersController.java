package com.hangman;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
public class GuessedLettersController implements Initializable {



    String  playerTwoName;

    public String results;
    @FXML
    private Label lbScore;

    @FXML
    private Button btnReturn;

    @FXML
    private Text txtRightGuesses;

    private Stage stage;
    private Scene scene;

    private Parent parent;

    @FXML
    public Text txtWrongLetters;

    @FXML
    public Text txtRightLetters;


    String wrongLetters;
    String rightLetters;



    @FXML
    private Button btnExit;


    public void start(Stage stage) throws IOException{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Show-guessed-letters.fxml"));
        //to override properties our scene builder app
        Scene scene = new Scene((Parent) fxmlLoader.load(), 600, 400);

    }
    @FXML
    private AnchorPane guessesLettersPane;

    public void ExitGame(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exiting game");
        alert.setHeaderText("Are you sure you want to exit the game?");


        //if a person clicks OK => exit, if they click CANCEL => stay
        if (alert.showAndWait().get() == ButtonType.OK)
        {
            stage = (Stage) guessesLettersPane.getScene().getWindow();
            System.out.println("You have exited the game!");
            stage.close();
        }


    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
