package com.hangman;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ShowResultsController implements Initializable {


    @FXML
    public Label txtNumberOfMoves;

    String numberOfMoves;
    @FXML
    private Button btnExitGame;
    @FXML
    public Text txtTheEnd;

    Stage stage;
    @FXML
    private AnchorPane ResultsPane;


    public void Exit(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exiting game");
        alert.setHeaderText("You are going to exit the game! Are you sure you want to exit?");


        //if a person clicks OK => exit, if they click CANCEL => stay
        if (alert.showAndWait().get() == ButtonType.OK)
        {
        stage = (Stage) ResultsPane.getScene().getWindow();
        System.out.println("You have exited the game!");
        stage.close();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
