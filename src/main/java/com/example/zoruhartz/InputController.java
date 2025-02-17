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
    private ComboBox<String> toothColor;
    @FXML
    private ComboBox<String> material;
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
            material.setValue(currentCase.getMaterial());
            toothColor.setValue(currentCase.getToothColor());
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
        toothColor.getItems().addAll(
                "A1", "A2", "A3", "A3.5", "A4",
                "B1", "B2", "B3", "B4",
                "C1", "C2", "C3", "C4",
                "D1", "D2", "D3", "D4"
        );

        material.getItems().addAll(
                "NEM", "G3", "T6"
        );
        material.setEditable(true);
    }

    private void updateProgress() {
        LocalDate currentDate = LocalDate.now();

        if (startDate != null && endDate != null) {
            long totalDays = ChronoUnit.DAYS.between(currentCase.getStartDate(), currentCase.getEndDate());

            long elapsedDays = ChronoUnit.DAYS.between(currentCase.getStartDate(), currentDate);

            if (elapsedDays < 0) {
                progressBar.setProgress(0);
            } else if (elapsedDays > totalDays) {
                progressBar.setProgress(1);
            } else {
                double progress = (double) elapsedDays / totalDays;
                progressBar.setProgress(progress);
            }
        } else {
            progressBar.setProgress(0);
        }
    }

    @FXML
    private void onSave() {
        if(!inputValidation()){
            validationLabel.setText("Some fields are empty or time is wrong format(hh:mm)");
            return;
        }
        String caseIdValue = caseId.getText();
        String nameValue = name.getText();
        String surnameValue = surname.getText();
        String descriptionValue = description.getText() ;
        String toothColorValue = toothColor.getValue();
        String materialValue = material.getValue();
        LocalDate startDateValue = startDate.getValue();
        System.out.println(descriptionValue);

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
                mainController.setCaseItem(index,currentCase);
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
        if (endDateTime.getText().isEmpty() ||  endDate.getValue() == null ||  startDate.getValue() == null ||
        caseId.getText().isEmpty() || name.getText().isEmpty() || surname.getText().isEmpty() ||
                description.getText().isEmpty()) {
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
        validationLabel.setText("Changed finished");
    }


}