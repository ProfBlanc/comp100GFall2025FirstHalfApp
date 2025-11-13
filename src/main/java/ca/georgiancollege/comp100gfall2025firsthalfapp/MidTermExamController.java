package ca.georgiancollege.comp100gfall2025firsthalfapp;

    
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.nio.file.*;
import java.util.*;

public class MidTermExamController {

        @FXML private ImageView imageView;
        @FXML private Button choiceA, choiceB, choiceC, choiceD, nextButton;
        @FXML private Label feedbackLabel,scoreLabel,roundLabel;

        private final MidTermExamModel model = new MidTermExamModel();
        private final MidTermExamService service = new MidTermExamService();

        private List<String> wordBank;
        private final Random rand = new Random();

        @FXML
        public void initialize() {
            wordBank = MidTermExamModel.loadWords();
            updateScoreDisplay();
            setChoicesDisabled(true);
            feedbackLabel.setText("Press 'Next Round' to start.");
        }


        // ------------------------------
        // Action Events only
        // ------------------------------
        public void handleNextRound(ActionEvent event) {
            feedbackLabel.setText("Loading new round...");
            setChoicesDisabled(true);
            nextButton.setDisable(true);

            model.nextRound();
            updateScoreDisplay();

            // 1. Pick correct word
            String correct = wordBank.get(rand.nextInt(wordBank.size()));
            model.setCorrectWord(correct);

            // 2. Generate 3 incorrect words
            Set<String> choices = new LinkedHashSet<>();
            choices.add(correct);
            while (choices.size() < 4) {
                String c = wordBank.get(rand.nextInt(wordBank.size()));
                if (!c.equalsIgnoreCase(correct)) choices.add(c);
            }

            List<String> choiceList = new ArrayList<>(choices);
            Collections.shuffle(choiceList);
            model.setChoices(choiceList);

            // 3. Fetch images from the service (no threads)
            try {
                List<String> images = service.fetchAndCacheImages(correct, 3);
                if (images != null && !images.isEmpty()) {
                    model.setImagePath(images.get(0));
                } else {
                    model.setImagePath(null);
                }
            } catch (Exception e) {
                feedbackLabel.setText("Error fetching image. Check API key or connection.");
                model.setImagePath(null);
            }

            // 4. Display everything now
            showRound();
        }

        private void showRound() {
            updateScoreDisplay();
            roundLabel.setText("Round: " + model.getRound());

            if (model.getImagePath() != null) {
                try {
                    Image img = new Image(Paths.get(model.getImagePath()).toUri().toString(),
                            700, 400, true, true);
                    imageView.setImage(img);
                } catch (Exception e) {
                    imageView.setImage(null);
                }
            } else {
                imageView.setImage(null);
            }

            List<String> ch = model.getChoices();
            choiceA.setText(ch.get(0));
            choiceB.setText(ch.get(1));
            choiceC.setText(ch.get(2));
            choiceD.setText(ch.get(3));

            // Connect choice buttons to checkAnswer
            choiceA.setOnAction(this::handleChoice);
            choiceB.setOnAction(this::handleChoice);
            choiceC.setOnAction(this::handleChoice);
            choiceD.setOnAction(this::handleChoice);

            feedbackLabel.setText("Choose the correct word.");
            setChoicesDisabled(false);
            nextButton.setDisable(false);
        }

        public void handleChoice(ActionEvent event) {
            Button clicked = (Button) event.getSource();
            String selected = clicked.getText();
            setChoicesDisabled(true);

            String correct = model.getCorrectWord();
            if (selected.equalsIgnoreCase(correct)) {
                model.incrementScore();
                feedbackLabel.setText("✅ Correct! +1 point.");
            } else {
                feedbackLabel.setText("❌ Incorrect! Correct word was: " + correct);
            }

            updateScoreDisplay();
        }

        private void updateScoreDisplay() {
            scoreLabel.setText("Score: " + model.getScore());
            roundLabel.setText("Round: " + model.getRound());
        }

        private void setChoicesDisabled(boolean disabled) {
            choiceA.setDisable(disabled);
            choiceB.setDisable(disabled);
            choiceC.setDisable(disabled);
            choiceD.setDisable(disabled);
        }
    }

