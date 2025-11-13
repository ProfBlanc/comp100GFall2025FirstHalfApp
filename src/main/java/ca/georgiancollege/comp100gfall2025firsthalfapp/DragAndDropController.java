package ca.georgiancollege.comp100gfall2025firsthalfapp;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.security.SecureRandom;
import java.util.stream.IntStream;

public class DragAndDropController {


    @FXML
    AnchorPane layout;
    @FXML
    Pane pane1, pane2, pane3;

    @FXML
    Label
    timer, score, message, category1, category2, category3;

    private int millisecond = 1000;
    private long timeStart = System.currentTimeMillis();

    private int maxTimeInSeconds, intervalInMS, timeBonus;

    private double orginalPositionX, orginalPositionY;


    private int numberOfRectanglesToAnswer, numOfCorrectAnswers, numOfIncorrectAnswers;
    Timeline timelineGameOver, timelineRemoveMessage, timelineRunTimer;

    public DragAndDropController(){

        maxTimeInSeconds = 200;
        intervalInMS = 100;
        timeBonus = 222;
    }
    @FXML
    private void initialize(){

        clearScoreboard();
        timedGame();
        //gameNumbers();
//        gameWordsLetters();

     gameWordsLength();
    }

    private void clearScoreboard(){

        timer.setText("");
        score.setText("0");
        message.setText("");

    }

    private void timedGame(){

        timer.setText(String.valueOf(maxTimeInSeconds));

/*
        Runnable task = ()->{
            double currentTime = Double.parseDouble(timer.getText());
            currentTime*=1000;

            currentTime -= intervalInMS;
            timer.setText(String.valueOf(currentTime));
        };
        Platform.runLater(task);

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(task, intervalInMS, intervalInMS, TimeUnit.MILLISECONDS);
*/


        EventHandler eh = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double currentTime = Double.parseDouble(timer.getText());
                currentTime*=millisecond;

                currentTime -= intervalInMS;
                currentTime/= millisecond;

                currentTime = Math.round(currentTime * 10) / 10.0;

                /*
                if(currentTime % 4 == 0)
                    calculateBonusPoints();
                */

                //System.out.println(currentTime);
                timer.setText(String.valueOf(currentTime));


            }
        };
        timelineRunTimer = new Timeline(new KeyFrame(Duration.millis(intervalInMS), eh));

        timelineRunTimer.setCycleCount(maxTimeInSeconds * millisecond / intervalInMS );
        timelineRunTimer.play();
    }

    private void gameWordsLength(){

        category1.setText("Len < 4");
        category2.setText("Len 4-6");
        category3.setText("Len 7+");



        String[] arr = {"shake","elide","wrack","beele","podiatry","siamang","subjoin","trommel"};
        int counter = 0;

        for(Node item : layout.getChildren()){

            if(item instanceof Label){

                Label current = ((Label)item);


                if(current.getText().equals("ABCDEFG")){
                    current.setText(arr[counter++]);
                    current.setOnMousePressed(this::pressedLabel);
                    current.setOnMouseReleased(this::releaseLabelWordLength);
                    current.setOnMouseDragged(this::dragLabel);

                    numberOfRectanglesToAnswer++;

                }
            }

        }

/*
        Runnable task = ()->{

            try{
                TimeUnit.SECONDS.sleep(maxTimeInSeconds);

            }
            catch (InterruptedException e){
                message.setText("");
            }
        };

        Platform.runLater(task);
  */
        EventHandler gameover = new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                layout.setDisable(true);
            }
        };



        timelineGameOver = new Timeline(new KeyFrame(Duration.seconds(maxTimeInSeconds), gameover));
        Platform.runLater(timelineGameOver::play);




    }
    private void gameNumbers(){

        category1.setText("# < 0");
        category2.setText("# < 50");
        category3.setText("# >= 50");

        SecureRandom secureRandom = new SecureRandom();
        IntStream ints = secureRandom.ints(8, -20, 150);

        int[] arr = ints.toArray();
        int counter = 0;

        for(Node item : layout.getChildren()){

            if(item instanceof Label){

                Label current = ((Label)item);
                if(current.getText().equals("ABCDEFG")){
                        current.setText(String.valueOf(arr[counter++]));
                        current.setOnMousePressed(this::pressedLabel);
                    current.setOnMouseReleased(this::releaseLabel);
                    current.setOnMouseDragged(this::dragLabel);

                    numberOfRectanglesToAnswer++;

                }
            }

        }

/*
        Runnable task = ()->{

            try{
                TimeUnit.SECONDS.sleep(maxTimeInSeconds);

            }
            catch (InterruptedException e){
                message.setText("");
            }
        };

        Platform.runLater(task);
  */
        EventHandler gameover = new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                layout.setDisable(true);
            }
        };



        timelineGameOver = new Timeline(new KeyFrame(Duration.seconds(maxTimeInSeconds), gameover));
        Platform.runLater(timelineGameOver::play);




    }

    @FXML
    private void pressedLabel(MouseEvent event){

        Label current = (Label)event.getSource();

        orginalPositionX = current.getLayoutX();
        orginalPositionY = current.getLayoutY();
        // System.out.println("orig = " + orginalPositionX + ", " + orginalPositionY );

    }
    @FXML
    private void dragLabel(MouseEvent event){

        // System.out.println("Dragged to " + event.getSceneX() + ", " + event.getSceneY());

        Label current = (Label)event.getSource();

        current.setLayoutX(event.getSceneX());
        current.setLayoutY(event.getSceneY());


    }
    @FXML
    private void releaseLabel(MouseEvent event){

        Label current = (Label)event.getSource();


        //System.out.println("Result = " + labelWithinPane(pane1, current.getLayoutX(), current.getLayoutY()));

        if(labelWithinPane(pane1, current.getLayoutX(), current.getLayoutY())){
            checkIfCorrect(1, current);
            addLabelToPane(pane1, current);
        }
        else if(labelWithinPane(pane2, current.getLayoutX(), current.getLayoutY())){
            checkIfCorrect(2, current);
            addLabelToPane(pane2, current);
        }
        else if(labelWithinPane(pane3, current.getLayoutX(), current.getLayoutY())){
            checkIfCorrect(3, current);
            addLabelToPane(pane3, current);
        }
        else{
            current.setLayoutX(orginalPositionX);
            current.setLayoutY(orginalPositionY);

        }

        orginalPositionX = 0;
        orginalPositionY = 0;

        checkToSeeIfGameIsOver();


    }
    private void releaseLabelWordLength(MouseEvent event){

        Label current = (Label)event.getSource();


        //System.out.println("Result = " + labelWithinPane(pane1, current.getLayoutX(), current.getLayoutY()));

        if(labelWithinPane(pane1, current.getLayoutX(), current.getLayoutY())){
            checkIfCorrectWordLength(1, current);
            addLabelToPane(pane1, current);
        }
        else if(labelWithinPane(pane2, current.getLayoutX(), current.getLayoutY())){
            checkIfCorrectWordLength(2, current);
            addLabelToPane(pane2, current);
        }
        else if(labelWithinPane(pane3, current.getLayoutX(), current.getLayoutY())){
            checkIfCorrectWordLength(3, current);
            addLabelToPane(pane3, current);
        }
        else{
            current.setLayoutX(orginalPositionX);
            current.setLayoutY(orginalPositionY);

        }

        orginalPositionX = 0;
        orginalPositionY = 0;

        checkToSeeIfGameIsOver();
    }

    private boolean labelWithinPane(Pane container, double x, double y){


        //System.out.println("X = " + x + " Container X = " + container.getLayoutX());

        return x >= container.getLayoutX() && x<= container.getLayoutX() + container.getWidth()
                &&
                y >= container.getLayoutY() && y<= container.getLayoutY() + container.getHeight();

    }
    private void checkIfCorrect(int i, Label current) {

        int value = Integer.parseInt(current.getText());
        switch (i){

            case 1:
                if(value < 0){
                    calculateBonusPoints();
                    splashMessage("Success!");
                    numOfIncorrectAnswers++;

                }
                else{
                    splashMessage("Error");
                    numOfIncorrectAnswers++;

                }
                break;

            case 2:
                if(value >= 0 && value < 50){
                    calculateBonusPoints();
                    splashMessage("Success!");
                    numOfIncorrectAnswers++;
                }
                else{
                    splashMessage("Error");
                    numOfIncorrectAnswers++;
                }
                break;
            case 3:
                if(value >= 50){
                    calculateBonusPoints();
                    splashMessage("Success!");
                    numOfIncorrectAnswers++;
                }
                else{
                    splashMessage("Error");
                    numOfIncorrectAnswers++;
                }
                break;
        }
    }
    private void checkIfCorrectWordLength(int i, Label current) {

        int value = current.getText().length();
        switch (i){

            case 1:
                if(value < 4){
                    calculateBonusPoints();
                    splashMessage("Success!");
                    numOfCorrectAnswers++;
                }
                else{
                    splashMessage("Error");
                    numOfIncorrectAnswers++;
                }
                break;

            case 2:
                if(value >= 4 && value < 6){
                    calculateBonusPoints();
                    splashMessage("Success!");
                    numOfCorrectAnswers++;
                }
                else{
                    splashMessage("Error");
                    numOfIncorrectAnswers++;
                }
                break;
            case 3:
                if(value >= 7){
                    calculateBonusPoints();
                    splashMessage("Success!");
                    numOfCorrectAnswers++;
                }
                else{
                    splashMessage("Error");
                    numOfIncorrectAnswers++;
                }
                break;
        }
    }
    private void addLabelToPane(Pane pane, Label label){

        int numChildren = pane.getChildren().size();

        numChildren++;
        System.out.println(numChildren);
        double tx = 10;
        double ty = pane.getHeight() - 10 - (label.getHeight() * numChildren + 5);
        label.setLayoutX(tx);
        label.setLayoutY(ty );
//        System.out.println(tx + ", " + ty);

        pane.getChildren().add(label);
        label.setOnMouseReleased(null);
        label.setOnMousePressed(null);
        label.setOnMouseDragged(null);
    }

    private void splashMessage(String text){

        message.setText(text);
        /*
        Runnable task = ()->{

            try{
                TimeUnit.SECONDS.sleep(1);
                message.setText("");
            }
            catch (InterruptedException e){
            }
        };

        Platform.runLater(task);
        */

        EventHandler removemessage = (ActionEvent) -> message.setText("");



        timelineRemoveMessage = new Timeline(new KeyFrame(Duration.seconds(3), removemessage));
        Platform.runLater(timelineRemoveMessage::play);


    }
    private void calculateBonusPoints(){

        long bonus = ( (System.currentTimeMillis() - timeStart)/1000L) * timeBonus;

        score.setText(String.valueOf(bonus));

    }
    private void checkToSeeIfGameIsOver(){
        System.out.println("****************");
        System.out.println(numOfCorrectAnswers);
        System.out.println(numOfIncorrectAnswers);
        System.out.println(numberOfRectanglesToAnswer);
        System.out.println("****************");

        if(numOfCorrectAnswers + numOfIncorrectAnswers == numberOfRectanglesToAnswer){


            StringBuilder message = new StringBuilder("Game is over");
            message.append("\n");

            if(numOfCorrectAnswers == numberOfRectanglesToAnswer)
                message.append("You win!");
            else
                message.append("You lose!");

            new Alert(Alert.AlertType.INFORMATION, message.toString(), ButtonType.OK).show();
            layout.setDisable(true);
            timelineGameOver.stop();
            timelineRemoveMessage.stop();
            timelineRunTimer.stop();

        }


    }


}
