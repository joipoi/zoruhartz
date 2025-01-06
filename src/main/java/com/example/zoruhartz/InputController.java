package com.example.zoruhartz;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class InputController {
    @FXML
    private TextField caseId;
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private DatePicker receiptDate;
    @FXML
    private TextField time;
    @FXML
    private TextArea description;
    @FXML
    private TextField toothColor;
    @FXML
    private TextField material;
    @FXML
    private ProgressBar progressBar;

    private boolean isNewCase;
    private Case currentCase;

    // No-argument constructor
    public InputController() {
        // Default initialization, if necessary
        System.out.println("In no-argument constructor");
    }

    // Setter method to pass data
    public void setData(Case currentCase, boolean isNewCase) {
        this.currentCase = currentCase;
        this.isNewCase = isNewCase;

        if (currentCase != null) {
            setData(currentCase);
            caseId.setDisable(true);
        }
    }

    public void setData(Case data) {
        if (data != null) {
            caseId.setText(data.getCaseId());
            name.setText(data.getName());
            surname.setText(data.getSurname());
            description.setText(data.getDescription());
            material.setText(data.getMaterial());
            toothColor.setText(data.getToothColor());
        }
    }

    @FXML
    public void initialize() {
        progressBar.setProgress(0.8);
        System.out.println("In initialize, currentCase: " + currentCase);
    }

    @FXML
    private void onSave() {
        // Gather input from UI elements
        String caseIdValue = caseId.getText();
        String nameValue = name.getText();
        String surnameValue = surname.getText();
        String descriptionValue = description.getText();
        String toothColorValue = toothColor.getText();
        String materialValue = material.getText();

        // Get the selected date and time
        LocalDateTime receiptDateTime = null;
        if (receiptDate.getValue() != null && !time.getText().isEmpty()) {
            String dateString = receiptDate.getValue().toString() + " " + time.getText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            receiptDateTime = LocalDateTime.parse(dateString, formatter);
        }

        if (currentCase != null) {
            currentCase.setName(nameValue);
            currentCase.setSurname(surnameValue);
            currentCase.setDescription(descriptionValue);
            currentCase.setReceiptDate(receiptDateTime);
            currentCase.setToothColor(toothColorValue);
            currentCase.setMaterial(materialValue);
            System.out.println("Updated case: " + currentCase);
        } else {
            Case newCase = new Case(caseIdValue, nameValue, surnameValue, descriptionValue,
                    receiptDateTime, toothColorValue, materialValue);
            MainController.addCase(newCase);
            System.out.println("Added new case: " + newCase);
        }

        Stage stage = (Stage) caseId.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void onFinishedButtonClick() {

    }
    @FXML
    private void exportCasesToCSV() {
      /*  // Create a FileChooser to select the export location
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Cases to CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Write header
                writer.write("Case ID,Name,Surname,Description,Receipt Date,Tooth Color,Material");
                writer.newLine();

                // Write each Case object
                for (Case caseObj : caseList) {
                    writer.write(String.format("%s,%s,%s,%s,%s,%s,%s",
                            caseObj.getCaseId(),
                            caseObj.getName(),
                            caseObj.getSurname(),
                            caseObj.getDescription(),
                            caseObj.getReceiptDate(),
                            caseObj.getToothColor(),
                            caseObj.getMaterial()));
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace(); // Handle exceptions appropriately
            }
        } */
    }

}