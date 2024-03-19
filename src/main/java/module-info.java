module com.example.hangman {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.naming;
    requires java.xml;


    opens com.hangman to javafx.fxml;
    exports com.hangman;
    exports com.hangman.rmiserver;
}