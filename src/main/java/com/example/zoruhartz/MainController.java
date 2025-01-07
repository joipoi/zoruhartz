package com.example.zoruhartz;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainController {
    @FXML
    private Label saveLabel;
    @FXML
    private ListView<Case> listView;


    public static ObservableList<Case> caseList = FXCollections.observableArrayList();

    public static void addCase(Case newCase) {
        caseList.add(newCase);
        System.out.println(caseList);
    }

    public static void removeCase(Case caseToRemove) {
        caseList.remove(caseToRemove);
    }

    private Case getCaseById(String caseId) {
        for (Case caseItem : caseList) {
            if (caseItem.getCaseId().equals(caseId)) {
                return caseItem;
            }
        }
        return null;
    }
    @FXML
    public void initialize() {
        listView.setItems(caseList);

        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Case item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Create a layout for the cell
                    HBox hbox = new HBox();
                    hbox.setSpacing(10); // Space between elements

                    // Create Text nodes for each piece of information
                    Text caseIdText = new Text("ID: " + item.getCaseId());
                    Text nameText = new Text("Name: " + item.getName());
                    Text surnameText = new Text("Surname: " + item.getSurname());
                    Text endDateText = new Text("End Date: " + item.getEndDate().toString());
                    Text finished = new Text(item.isFinished() ? "Finished" : "Not Finished");

                    // Add Text nodes to the HBox
                    hbox.getChildren().addAll(caseIdText, nameText, surnameText, endDateText, finished);

                    // Set the HBox as the graphic for the cell
                    setGraphic(hbox);

                    if (item.isFinished()) {
                        setStyle("-fx-background-color: lightgreen;"); // Color for finished
                    } else {
                        setStyle("-fx-background-color: lightcoral;"); // Color for not finished
                    }
                }
            }
        });
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Single click
                Case selectedCase = listView.getSelectionModel().getSelectedItem();
                if (selectedCase != null) {
                    openNewWindow(selectedCase, false);
                }
            }
        });
        File file = new File("src/main/resources/test.csv");
        caseList.clear(); // Optional: clear existing cases
        caseList.addAll(importCasesFromCSV(file));

    }

    @FXML
    private void onAddCase(){
     openNewWindow(null, true);
    }
    @FXML
    private void onSave(){
        exportCasesToCSV("src/main/resources/test.csv");
        saveLabel.setText("Data saved");
    }
    @FXML
    private void onExport(){
        exportCasesToCSV(null);
    }

    private void openNewWindow(Case selectedItem, boolean isNewCase) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("input-view.fxml"));
            GridPane newWindowRoot = loader.load();

            Stage newWindow = new Stage();
            newWindow.setTitle("Case List");
            newWindow.setScene(new Scene(newWindowRoot));

            InputController newWindowController = loader.getController();
            newWindowController.setData(isNewCase ? null : selectedItem, isNewCase);

            newWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void importCasesFromCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            caseList.clear();
            caseList.addAll(importCasesFromCSV(file));
        }
    }

    @FXML
    private void exportCasesToCSV(String filename) {
        File file;
        if(filename == null){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Cases to CSV");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            file = fileChooser.showSaveDialog(null);
        }else{
            file = new File(filename);
        }

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("Case ID,Name,Surname,Description,Start Date,End Date,Tooth Color,Material");
                writer.newLine();

                for (Case caseObj : caseList) {
                    writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                            caseObj.getCaseId(),
                            caseObj.getName(),
                            caseObj.getSurname(),
                            caseObj.getDescription(),
                            caseObj.getStartDate(),
                            caseObj.getEndDate(),
                            caseObj.getToothColor(),
                            caseObj.getMaterial()));
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Case> importCasesFromCSV(File file) {
        List<Case> cases = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields.length == 8) {
                    String caseId = fields[0];
                    String name = fields[1];
                    String surname = fields[2];
                    String description = fields[3];

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate startDate = LocalDate.parse(fields[4], formatter);

                    LocalDateTime endDate = LocalDateTime.parse(fields[5], dateTimeFormatter);
                    String toothColor = fields[6];
                    String material = fields[7];

                    Case caseObj = new Case(caseId, name, surname, description, startDate, endDate, toothColor, material, false);
                    cases.add(caseObj);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cases;
    }

}
