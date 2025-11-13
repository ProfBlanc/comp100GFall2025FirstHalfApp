package ca.georgiancollege.comp100gfall2025firsthalfapp;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MidTermExamModel {
    private String correctWord;
    private List<String> choices; // 4 choices
    private String imagePath; // local image path to display
    private int score = 0;
    private int round = 0;

    public String getCorrectWord() { return correctWord; }
    public void setCorrectWord(String correctWord) { this.correctWord = correctWord; }

    public List<String> getChoices() { return choices; }
    public void setChoices(List<String> choices) { this.choices = choices; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public int getScore() { return score; }
    public void incrementScore() { score++; }

    public int getRound() { return round; }
    public void nextRound() { round++; }

    /**
     * Reads the words.txt file and generates an arraylist containing all words found in the .txt file
     * @return an arraylist containing all words found in the .txt file
     */
    public static List<String> loadWords() {

        List<String> wordBank = new ArrayList<>();

        try (InputStream in = MidTermExamModel.class.getResourceAsStream("/data/words.txt")) {
            if (in != null) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                    wordBank = br.lines().filter(l -> !l.isBlank()).map(String::trim).collect(Collectors.toList());
                }
            }
        } catch (Exception ignored) {}

        if (wordBank.isEmpty()) {
            wordBank = Arrays.asList("apple", "banana", "dog", "cat", "car", "beach", "soccer", "guitar", "pizza", "mountain");
        }

        return wordBank;
    }

}
