package ca.georgiancollege.comp100gfall2025firsthalfapp;

import javafx.scene.control.Alert;


public class Utilities {
    public static void showInfoAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Information");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}