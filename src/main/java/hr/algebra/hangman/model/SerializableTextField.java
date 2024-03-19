package hr.algebra.hangman.model;

import javafx.scene.control.TextField;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class SerializableTextField implements Serializable {

    private StringBuilder hiddenWord;

    private String rightLetters;
    private String wrongLetters;
    private Integer points;
    private List<String> lives;



    public SerializableTextField(StringBuilder hiddenWord,Integer points,List<String> lives,String rightLetters,String wrongLetters) {
        this.hiddenWord = hiddenWord;
        this.points = points;
        this.lives = lives;
        this.rightLetters = rightLetters;
        this.wrongLetters = wrongLetters;
    }

    public SerializableTextField(String hidden_word_toString, String points_toString, String lives_toString, String right_letters_toString, String wrong_letters_toString) {
    this.hiddenWord = new StringBuilder(hidden_word_toString);
    this.points = Integer.valueOf(points_toString);
    this.lives = Collections.singletonList(lives_toString);
    this.rightLetters = right_letters_toString;
    this.wrongLetters = wrong_letters_toString;
    }


    public StringBuilder getHiddenWord() {
        return hiddenWord;
    }


    public Integer getPoints() {
        return points;
    }

    public List<String> getLives() {
        return lives;
    }

    public String getRightLetters() {
        return rightLetters;
    }

    public String getWrongLetters() {
        return wrongLetters;
    }
}
