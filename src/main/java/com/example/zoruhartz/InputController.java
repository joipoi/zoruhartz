package com.example.zoruhartz;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;


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
    @FXML
    private Button finishedBtn;
    @FXML
    private Label validationLabel;



    private Case currentCase;

    private MainController mainController;

    // No-argument constructor
    public InputController() {
    }

    public void setMainController(MainController controller) {
        this.mainController = controller; // Store the reference
    }

    public void setData(Case currentCase) {
        this.currentCase = currentCase;
        if (currentCase != null) {
            caseId.setText(currentCase.getCaseId());
            name.setText(currentCase.getName());
            surname.setText(currentCase.getSurname());
            description.setText(currentCase.getDescription());
            material.setText(currentCase.getMaterial());
            toothColor.setText(currentCase.getToothColor());
            startDate.setValue(currentCase.getStartDate());
            endDate.setValue(LocalDate.from(currentCase.getEndDate()));

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            endDateTime.setText(currentCase.getEndDate().format(timeFormatter));
            updateProgress();

            caseId.setDisable(true);
            if(currentCase.isFinished()){
                finishedBtn.setText("Regret Finished");
            }

        }else{
            progressBar.setVisible(false);
            finishedBtn.setVisible(false);

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
        if(!inputValidation()){
            validationLabel.setText("Some fields are empty or time is wrong format(hh:mm)");
            return;
        }
        // Gather input from UI elements
        String caseIdValue = caseId.getText();
        String nameValue = name.getText();
        String surnameValue = surname.getText();
        String descriptionValue = description.getText() ;
        String toothColorValue = toothColor.getText();
        String materialValue = material.getText();
        LocalDate startDateValue = startDate.getValue();
        System.out.println(descriptionValue);

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

            int index = mainController.getCaseList().indexOf(currentCase);
            if (index != -1) {
                mainController.setCaseItem(index,currentCase); // Replace the item
            }
        } else {
            Case newCase = new Case(caseIdValue, nameValue, surnameValue, descriptionValue,
                    startDateValue, receiptDateTime, toothColorValue, materialValue, false);
            mainController.addCase(newCase);
            System.out.println("Added new case: " + newCase);
        }

        Stage stage = (Stage) caseId.getScene().getWindow();
        stage.close();

    }
    private boolean inputValidation(){
        //check for empty fields
        if (endDateTime.getText().isEmpty() ||  endDate.getValue() == null ||  startDate.getValue() == null ||
        caseId.getText().isEmpty() || name.getText().isEmpty() || surname.getText().isEmpty() || material.getText().isEmpty() ||
                description.getText().isEmpty() || toothColor.getText().isEmpty()) {
            return false;
        }
       /* //check dates make sense
        if(endDate.getValue().isBefore(startDate.getValue())){
        return false;
        }*/

        //check time is in right format
        Pattern timePattern = Pattern.compile("^(?:[01]\\d|2[0-3]):[0-5]\\d$");
        return timePattern.matcher(endDateTime.getText()).matches();
    }
    @FXML
    private void onFinishedButtonClick() {
    currentCase.setFinished(!currentCase.isFinished());
    }


}