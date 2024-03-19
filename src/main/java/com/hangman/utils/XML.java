package com.hangman.utils;

import java.util.List;
import hr.algebra.hangman.model.SerializableTextField;
import javafx.scene.control.Alert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;


public class XML {

    public static void saveXML
            (StringBuilder hiddenWord, Integer points, List<String> lives,
             String rightLetters, String wrongLetters){

        try {
            DocumentBuilderFactory documentBuilderFactory
                    = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder
                    = documentBuilderFactory.newDocumentBuilder();
            Document xmlDocument = documentBuilder.newDocument();

            //first => parent is Hangman game
            Element rootElementGame = xmlDocument.createElement("Hangman_game");
            xmlDocument.appendChild(rootElementGame);

            //adding a new element => appending it and adding to root element
            // (with append we add hidden word in the middle)

            Element hidden_Word_Element = xmlDocument.createElement("Hidden_word");
            Node hidden_Word_TextNode = xmlDocument.createTextNode(hiddenWord.toString());
            hidden_Word_Element.appendChild(hidden_Word_TextNode);
            rootElementGame.appendChild(hidden_Word_Element);


            Element points_Element = xmlDocument.createElement("Points_for_words");
            Node points_TextNode = xmlDocument.createTextNode(points.toString());
            points_Element.appendChild(points_TextNode);
            rootElementGame.appendChild(points_Element);

            Element hangman_lives_Element = xmlDocument.createElement("Hangman_lives");
            Node hangman_lives_TextNode = xmlDocument.createTextNode(lives.toString());
            hangman_lives_Element.appendChild(hangman_lives_TextNode);
            rootElementGame.appendChild(hangman_lives_Element);


            Element guessed_right_letters_Element = xmlDocument.createElement("Guessed_right_letters");
            Node guessed_right_letters_TextNode = xmlDocument.createTextNode(rightLetters.toString());
            guessed_right_letters_Element.appendChild(guessed_right_letters_TextNode);
            rootElementGame.appendChild(guessed_right_letters_Element);

            Element guessed_wrong_letters_Element = xmlDocument.createElement("Guessed_wrong_letters");
            Node guessed_wrong_letters_TextNode = xmlDocument.createTextNode(wrongLetters.toString());
            guessed_wrong_letters_Element.appendChild(guessed_wrong_letters_TextNode);
            rootElementGame.appendChild(guessed_wrong_letters_Element);


            //Transformer => transforms element into xml dat
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            //DOMSource => DOMMaster,  takes that xml element i.e. root element
            Source xmlSource = new DOMSource(xmlDocument);
            Result xmlResult = new StreamResult(new File("Bingo5X5.xml"));

            //Convert xml object structure to xml file
            transformer.transform(xmlSource, xmlResult);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("XML file created!");
            alert.setHeaderText("XML file was successfully created!");
            alert.setContentText("File 'Hangman.xml' was created!");

            alert.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static SerializableTextField loadXML() {

        SerializableTextField serializableTextField = null;
        try {
            File HangmanFile = new File("Hangman.xml");
            InputStream HangmanStream = new FileInputStream(HangmanFile);

            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmlDocument = parser.parse(HangmanStream);

            String rootNodeName = xmlDocument.getDocumentElement().getNodeName();
            NodeList nodeList = xmlDocument.getElementsByTagName("Hangman_game");

            for (int i = 0; i < nodeList.getLength(); i++) {

                Node gameNode = nodeList.item(i);

                if (gameNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element gameElement = (Element) gameNode;

                    String hidden_Word_ToString= gameElement
                            .getElementsByTagName("Hidden_word")
                            .item(0)
                            .getTextContent();
                    System.out.println("Hidden_word: " + hidden_Word_ToString);


                    String points_ToString = gameElement
                            .getElementsByTagName("Points")
                            .item(0)
                            .getTextContent();
                    System.out.println("Points: " + points_ToString);


                    String lives_ToString = gameElement
                            .getElementsByTagName("Lives")
                            .item(0)
                            .getTextContent();
                    System.out.println("Lives: " + lives_ToString);


                    String right_Letters_ToString = gameElement
                            .getElementsByTagName("Right_Letters")
                            .item(0)
                            .getTextContent();
                    System.out.println("Right_Letters: " + right_Letters_ToString);


                    String wrong_Letters_ToString = gameElement
                            .getElementsByTagName("Wrong_Letters")
                            .item(0)
                            .getTextContent();
                    System.out.println("Wrong_Letters: " + wrong_Letters_ToString);


                    serializableTextField = new SerializableTextField(hidden_Word_ToString, points_ToString,
                            lives_ToString, right_Letters_ToString, wrong_Letters_ToString);
                }
            }


        } catch (Exception e) {
            System.out.println("An error occurred while reading from the file!");
        }

        return serializableTextField;
    }



}
