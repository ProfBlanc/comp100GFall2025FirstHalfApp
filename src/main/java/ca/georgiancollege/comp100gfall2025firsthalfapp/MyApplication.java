package ca.georgiancollege.comp100gfall2025firsthalfapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MyApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource("drag-and-drop-game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Drag & Drop Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}