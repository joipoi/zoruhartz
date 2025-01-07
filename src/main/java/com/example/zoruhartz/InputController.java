package com.example.zoruhartz;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


public class InputController {
    @FXML
    private TextField caseId;
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private TextField endDateTime;
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
            startDate.setValue(LocalDate.from(data.getStartDate()));
            endDate.setValue(LocalDate.from(data.getEndDate()));

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            endDateTime.setText(data.getEndDate().format(timeFormatter));
            updateProgress();
        }
    }

    @FXML
    public void initialize() {

    }

    private void updateProgress() {
        LocalDate currentDate = LocalDate.now();

        // Ensure startDate and endDate are not null
        if (startDate != null && endDate != null) {
            // Calculate total duration
            long totalDays = ChronoUnit.DAYS.between(currentCase.getStartDate(), currentCase.getEndDate());

            // Calculate elapsed duration
            long elapsedDays = ChronoUnit.DAYS.between(currentCase.getStartDate(), currentDate);

            // Ensure progress does not exceed 100%
            if (elapsedDays < 0) {
                // Case has not started yet
                progressBar.setProgress(0);
            } else if (elapsedDays > totalDays) {
                // Case has already ended
                progressBar.setProgress(1);
            } else {
                // Calculate progress as a fraction
                double progress = (double) elapsedDays / totalDays;
                progressBar.setProgress(progress);
            }
        } else {
            progressBar.setProgress(0); // Default to 0 if dates are not set
        }
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
        LocalDate startDateValue = startDate.getValue();

        // Get the selected date and time
        LocalDateTime receiptDateTime = null;
        if (endDate.getValue() != null && !endDateTime.getText().isEmpty()) {
            String dateString = endDate.getValue().toString() + " " + endDateTime.getText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            receiptDateTime = LocalDateTime.parse(dateString, formatter);
        }

        if (currentCase != null) {
            currentCase.setName(nameValue);
            currentCase.setSurname(surnameValue);
            currentCase.setDescription(descriptionValue);
            currentCase.setToothColor(toothColorValue);
            currentCase.setMaterial(materialValue);
            currentCase.setStartDate(startDateValue);
            currentCase.setEndDate(receiptDateTime);
            System.out.println("Updated case: " + currentCase);

            int index = MainController.caseList.indexOf(currentCase);
            if (index != -1) {
                MainController.caseList.set(index, currentCase); // Replace the item
            }
        } else {
            Case newCase = new Case(caseIdValue, nameValue, surnameValue, descriptionValue,
                    startDateValue, receiptDateTime, toothColorValue, materialValue, false);
            MainController.addCase(newCase);
            System.out.println("Added new case: " + newCase);
        }

        Stage stage = (Stage) caseId.getScene().getWindow();
        stage.close();

    }
    @FXML
    private void onFinishedButtonClick() {
    currentCase.setFinished(true);
    }


}