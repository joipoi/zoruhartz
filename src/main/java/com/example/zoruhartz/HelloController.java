package com.example.zoruhartz;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class HelloController {
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label welcomeText;

    @FXML
    private Label label;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    public void initialize() {
        // Set the progress to 80% by default
        progressBar.setProgress(0.8); // Value between 0.0 and 1.0
    }
}