package com.hangman;

import com.hangman.rmiserver.ChatService;
import com.hangman.utils.XML;
import hr.algebra.hangman.model.PlayerDetails;
import hr.algebra.hangman.model.SerializableTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import javax.naming.NamingException;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GameController implements Initializable {

    @FXML
    public Text txtPlayerName;

    @FXML
    private Button btnShowAllMoves;

    @FXML
    private Button btnShowResults;

    @FXML
    private Text txttextForWord;
    @FXML
    private TextField tfInsertedWord;

    @FXML
    private Text txtHangmanLives;

    @FXML
    public Text txtTheEnd;

    public String word;

    //StringBuilder => creates a mutable sequence of characters
    private StringBuilder insertedHiddenWord = new StringBuilder();
    private Integer livesSpot = 0;

    private Integer pointsForLetter = 0;

    @FXML
    public Text txtWrongLetters;

    @FXML
    public Text txtRightLetters;

    @FXML
    public  Text txtNumberOfMoves;


    private SerializableTextField serializableTextFields;

    //for XML
    private SerializableTextField serializableTextFieldXML;

    //for RMI

    @FXML
    private TextArea chatHistoryTextArea;
    @FXML
    private TextField chatMessageTextField;




    // hangman design, so I can use it in arraylist
    ArrayList<String> lives = new ArrayList<>(Arrays.asList(
            """
                    ___________
                    |/                    
                    |
                    |
                    |
                    |
                    |
                    ----------------- """,
            """
                    ___________
                    |/        |
                    |
                    |
                    |
                    |
                    |
                    ----------------- """,
            """
                    ___________
                    |/         |
                    |          O
                    |
                    |
                    |
                    |
                    ----------------- """,
            """
                    ___________
                    |/         |
                    |          O
                    |          |
                    |
                    |
                    |
                    ----------------- """,
            """
                    ____________
                    |/          |
                    |           O
                    |           |
                    |          /
                    |
                    |
                    ---------------- """,
            """
                    ___________
                    |/         |
                    |          O
                    |          |
                    |         /|
                    |
                    |
                    ----------------- """,
            """
                    ___________
                    |/         |
                    |          O
                    |          |
                    |         /|/
                    |
                    |
                    ----------------- """,
            """
                    ___________
                    |/         |
                    |          O
                    |          |
                    |         /|/
                    |         /
                    |
                    ----------------- """,
            """
                    ___________
                    |/         |
                    |          O
                    |          |
                    |         /|/
                    |         //
                    |
                    ----------------- """));

    private ChatService stub = null;

    public PlayerDetails playerDetails;

    //player1 is inserting a word that player2 must guess
    @FXML
    void getInsertedWord(ActionEvent event) {
        if (word == null) {
            word = tfInsertedWord.getText();
            // the method to revel word and its length
            insertedWord();
            //hide inserted word
            tfInsertedWord.clear();
        } else {
          //  tfInsertedWord.setText(word);
            playersTurn();


        }
        tfInsertedWord.clear();


        //saving the entered word that will player #2 guess

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("FileForWords.txt"));
            writer.write(word);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    //when we insert text "test" => $$$$
    public void insertedWord() {
        int insertedWordLength = word.length();

        insertedHiddenWord.append("$".repeat(insertedWordLength));
        txttextForWord.setText(String.valueOf(insertedHiddenWord));

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        txtHangmanLives.setText(lives.get(livesSpot));

        try {
            //Registry registry = LocateRegistry.getRegistry();

            Registry registry = LocateRegistry.createRegistry( 1099);
            stub = (ChatService) registry.lookup(ChatService.REMOTE_OBJECT_NAME);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry("localhost", 1099);
            //niti for refreshing textArea
            new Thread(() -> refreshMessage()).start();
            ChatService stub = (ChatService) registry.lookup(ChatService.REMOTE_OBJECT_NAME);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }


    }

    //niti

    public void refreshMessage(){
        while (true){
            try {
                Thread.sleep(1000);
                chatHistoryTextArea.clear();

                fillTextAreaWithMessages();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void sendMessage() throws RemoteException {

        String newMessage = chatMessageTextField.getText();

            stub.sendMessage(newMessage, Controller.getPlayerName());

            stub.sendMessage(newMessage, playerDetails.getPlayerName());

            fillTextAreaWithMessages();

            chatMessageTextField.clear();


    }

    private void fillTextAreaWithMessages()
    {
        StringBuilder allMessagesBuilder = new StringBuilder();
        stub.receiveAllMessages().forEach(m -> allMessagesBuilder.append(m + "\n"));
        chatHistoryTextArea.setText(allMessagesBuilder.toString());
    }


    //when I have my turn,I would like to activate it
    public void playersTurn() {
        String letterToGuess = this.tfInsertedWord.getText();
        ArrayList<Integer> letterSpot = new ArrayList<>();

        char[] letterInAWord = word.toCharArray();
        char letterGuess = letterToGuess.charAt(0);



        // if all letters are in the entered word => player won the game
        if (word.equals(letterToGuess)) {

            txtTheEnd.setText("You won the game! Congrats!!!");
            txttextForWord.setText(letterToGuess);

            //if they insert the whole word => give maximum points
            pointsForLetter = pointsForLetter + word.length();
            txtNumberOfMoves.setText(String.valueOf(pointsForLetter));
            return;
        }


        //checking if inserted letter is in entered word
        if (word.contains(letterToGuess)) {
            for (int i = 0; i < word.length(); i++) {

                //if the letter is in an entered word, it will show in which position it is [0,2]
                if (letterInAWord[i] == letterGuess ) {
                    letterSpot.add(i);

                    //for every guessed letter you get a point
                    pointsForLetter++;

                }
            }

            //test => t=> t$$t
            letterSpot.forEach(spot -> {
                insertedHiddenWord.setCharAt(spot, letterGuess);
            });

            txttextForWord.setText(String.valueOf(insertedHiddenWord));
            txtNumberOfMoves.setText(String.valueOf(pointsForLetter));

            //getting right letters inside right letter container
            txtRightLetters.setText(txtRightLetters.getText() + String.valueOf((letterGuess + ",")));


            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("RightLetterFile.txt"));
                writer.write(txtRightLetters.getText() );
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }



        }
        //if it is not containing a letter => update my hangman design
        else {
            txtHangmanLives.setText(lives.get(++livesSpot));

            //getting wrong letters inside wrong letter container
            txtWrongLetters.setText(txtWrongLetters.getText() + String.valueOf((letterGuess + ",")));


            //saving the entered wrong letter that will player #2 entered

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("WrongLetterFile.txt"));
                writer.write(txtWrongLetters.getText());
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            //checking if the position is on 8 (last leg) => game over
            if (livesSpot == 8) {
                txtTheEnd.setText("You lost! Game over!");

            }
        }


    }

    //I need to reset the inserted word, if it is nothing => restart the game (try again)
    @FXML
    void reset(ActionEvent event) {


        word = null;
        insertedHiddenWord.setLength(0);
        livesSpot = 0;
        pointsForLetter = 0;
        txtHangmanLives.setText(lives.get(0));
        txtTheEnd.setText("");
        txtWrongLetters.setText("");
        txtRightLetters.setText("");

        //saving data into xml
        XML.saveXML(insertedHiddenWord, pointsForLetter,
                lives, txtWrongLetters.getText(),
                txtRightLetters.getText());

    }


        //getting another value and a controller
    public void ShowResults() throws  IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("show-results.fxml"));
        Parent root = fxmlLoader.load();

        ShowResultsController showResultsController = fxmlLoader.getController();
        showResultsController.txtNumberOfMoves.setText(txtNumberOfMoves.getText());
        showResultsController.txtTheEnd.setText(txtTheEnd.getText());


       Scene scene = new Scene(root);

        Main.getMainStage().setTitle("Results");
        Main.getMainStage().setScene(scene);
        Main.getMainStage().show();

        System.out.println("Player one " + Controller.getPlayerDetails().getPlayerName()
                + " has won ");


        PlayerDetails player = Controller.getPlayerDetails();
        displayVictoryDialog(player.getPlayerName());
    }

    private void displayVictoryDialog(String playerName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("You won!");
        alert.setContentText("Player " + playerName + " won!");

        alert.showAndWait();
    }


    public void ShowAllMoves()  throws  IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Show-guessed-letters.fxml"));
        Parent root = fxmlLoader.load();

        GuessedLettersController guessedLettersController = fxmlLoader.getController();
        guessedLettersController.txtWrongLetters.setText(txtWrongLetters.getText());
        guessedLettersController.txtRightLetters.setText(txtRightLetters.getText());

        Scene scene = new Scene(root);
        Main.getMainStage().setTitle("Guessed letters");
        Main.getMainStage().setScene(scene);
        Main.getMainStage().show();
    }


    //serialization
    public void saveGame() throws IOException {

        serializableTextFields = new SerializableTextField(insertedHiddenWord,pointsForLetter,lives,txtRightLetters.getText(),txtWrongLetters.getText());

        try( ObjectOutputStream serializer = new ObjectOutputStream( new FileOutputStream("savedGame.ser"))){;

            serializer.writeObject(serializableTextFields);
            System.out.println("Game is saved.");
        }

    }


    public void loadGame() throws IOException, ClassNotFoundException {
        try (ObjectInputStream deserializer = new ObjectInputStream(new FileInputStream("savedGame.ser"))) {
            serializableTextFields = (SerializableTextField) deserializer.readObject();

            //without this it adds the same hidden word
            insertedHiddenWord.delete(0,insertedHiddenWord.length());

            insertedHiddenWord.append(serializableTextFields.getHiddenWord());
            pointsForLetter.equals(serializableTextFields.getPoints());
            lives.add(serializableTextFields.getLives().toString());
            txtRightLetters.setText(serializableTextFields.getRightLetters());
            txtWrongLetters.setText(serializableTextFields.getWrongLetters());


            //loading xml
            serializableTextFieldXML = XML.loadXML();


    }}



    //listing every class and constructor etc.
    private static final String CLASS_EXTENSION = ".class";
    public void generateDocumentation() {

        //Generating in this folder
        File documentationFile = new File("documentation.html");

        try {

            FileWriter writer = new FileWriter(documentationFile);

            writer.write("<!DOCTYPE html>");
            writer.write("<html>");
            writer.write("<head>");
            writer.write("<title>Project documentation for Hangman</title>");
            writer.write("</head>");
            writer.write("<body>");
            writer.write("<h1>Project documentation</h1>");
            writer.write("<p><u><b>Class list:</b></u></p>");

            //fitler the data and print them
            List<Path> paths = Files.walk(Paths.get("."))
                    .filter(path -> path.getFileName().toString().endsWith(CLASS_EXTENSION))
                    .collect(Collectors.toList());

            //split with /
            for (Path path : paths) {
                String[] tokens = path.toString().split(Pattern.quote(System.getProperty("file.separator")));

                Boolean startBuildingPath = false;

                StringBuilder sb = new StringBuilder();

                for (String token : tokens) {
                    if ("classes".equals(token)) {
                        startBuildingPath = true;
                        continue;
                    }

                    if (startBuildingPath) {

                        if (token.endsWith(CLASS_EXTENSION)) {
                            sb.append(token.substring(0, token.indexOf(".")));
                        } else {
                            sb.append(token);
                            sb.append(".");
                        }
                    } else {
                        continue;
                    }
                }

                if ("module-info".equals(sb.toString())) {
                    continue;
                }

                System.out.println("Fully qualified name: " + sb.toString());

                try {
                    Class<?> clazz = Class.forName(sb.toString());

                    writer.write("<h2>" + Modifier.toString(clazz.getModifiers()) + " " + clazz.getName() + "</h2>");

                    StringBuilder classFieldString = new StringBuilder();

                    //anotations & class fields
                    for (Field classField : clazz.getDeclaredFields()) {
                        Annotation[] annotations = classField.getAnnotations();
                        if (annotations.length != 0) {
                            for (Annotation a : annotations) {
                                classFieldString.append(a.toString());
                                classFieldString.append("<br />");
                            }
                        }
                        classFieldString.append(Modifier.toString(classField.getModifiers()));
                        classFieldString.append(" ");
                        classFieldString.append(classField.getType().getSimpleName());
                        classFieldString.append(" ");
                        classFieldString.append(classField.getName());
                        classFieldString.append(" ");
                        classFieldString.append("<br /><br />");
                    }

                    writer.write("<h3><u>Fields</u></h3>");

                    writer.write("<h4>" + classFieldString + "</h4>");

                    //constructors

                    Constructor[] constructors = clazz.getConstructors();

                    writer.write("<h3><u>Constructors:</u></h3>");

                    for (Constructor c : constructors) {
                        String constructorParams = generateDocumentation(c);
                        writer.write("<h4>Constructor:" + Modifier.toString(c.getModifiers()) + " " + c.getName()
                                + "(" + constructorParams + ")" + "</h4>");
                    }


                    //methods
                    Method[] methods = clazz.getMethods();
                    writer.write("<h3><u>Methods:</u></h3>");
                    for (Method m : methods) {
                        String methodsParams = generateDocumentation(m);
                        StringBuilder exceptionsBuilder = new StringBuilder();

                        //exceptions
                        for (int i = 0; i < m.getExceptionTypes().length; i++) {
                            if (exceptionsBuilder.isEmpty()) {
                                exceptionsBuilder.append(" throws ");
                            }

                            Class exceptionClass = m.getExceptionTypes()[i];
                            exceptionsBuilder.append(exceptionClass.getSimpleName());

                            if (i < m.getExceptionTypes().length - 1) {
                                exceptionsBuilder.append(", ");
                            }
                        }

                        writer.write("<h4>Method:" + Modifier.toString(m.getModifiers())
                                + " " + m.getReturnType().getSimpleName()
                                + " " + m.getName() + "(" + methodsParams + ")"
                                + " " + exceptionsBuilder.toString()
                                + "</h4>");
                    }

                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            writer.write("</body>");
            writer.write("</html>");
            writer.close();

        } catch (IOException e) {
            //errors
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error while generating documentation!");
            alert.setHeaderText("Can't find the files");
            alert.setContentText("The class files cannot be accessed.");

            alert.showAndWait();
        }
    }

    //method that can only accept methods and constructors => for Parameters
    private <T extends Executable> String generateDocumentation(T executable) {
        Parameter[] params = executable.getParameters();

        StringBuilder methodsParams = new StringBuilder();

        for (int i = 0; i < params.length; i++) {
            String modifierString = Modifier.toString(params[i].getModifiers());

            if (!modifierString.isEmpty()) {
                methodsParams.append(modifierString);
                methodsParams.append(" ");
            }
            methodsParams.append(params[i].getType().getSimpleName());
            methodsParams.append(" ");
            methodsParams.append(params[i].getName());

            if (i < (params.length - 1)) {
                methodsParams.append(", ");
            }
        }

        return methodsParams.toString();
    }


    }

