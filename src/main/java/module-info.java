module ca.georgiancollege.comp100gfall2025firsthalfapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens ca.georgiancollege.comp100gfall2025firsthalfapp to javafx.fxml, com.google.gson;
    exports ca.georgiancollege.comp100gfall2025firsthalfapp;
}