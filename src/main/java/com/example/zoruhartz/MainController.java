package com.example.zoruhartz;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML
    private Label saveLabel;
    @FXML
    private ListView<Case> listView;


    private ObservableList<Case> caseList = FXCollections.observableArrayList();

    public ObservableList<Case> getCaseList() {
        return caseList;
    }
    public void addCase(Case _case){
        caseList.add(_case);
    }
    public void setCaseItem(int index, Case newCase) {
        if (index >= 0 && index < caseList.size()) {
            caseList.set(index, newCase);
        } else {
            System.out.println("Index out of bounds: " + index);
        }
    }

    @FXML
    public void initialize() {
        listView.setItems(caseList);

        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Case item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setStyle(null);
                } else {
                    HBox hbox = new HBox();
                    hbox.getChildren().clear();
                    hbox.setSpacing(10);

                    Text caseIdText = new Text("ID: " + item.getCaseId());
                    Text nameText = new Text("Name: " + item.getName() +" "+ item.getSurname());
                    Text endDateText = new Text("End Date: " + item.getEndDate().toString());
                    Text finished = new Text(item.isFinished() ? "Finished" : "Not Finished");
                    /*
                    Button deleteButton = new Button("Delete");
                    deleteButton.setOnAction(event -> {
                        caseList.removeCase(item);
                        System.out.println(caseList);
                        listView.setItems(caseList);
                    }); */

                    hbox.getChildren().addAll(caseIdText, nameText, endDateText, finished);

                    setGraphic(hbox);

                    if (item.isFinished()) {
                        setStyle("-fx-background-color: lightgreen;");
                    } else {
                        setStyle("-fx-background-color: lightcoral;");
                    }
                }
            }

        });
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Single click
                Case selectedCase = listView.getSelectionModel().getSelectedItem();
                if (selectedCase != null) {
                    openInputWindow(selectedCase);
                }
            }
        });
        Path filePath = Paths.get("src/main/resources/data.csv");
       caseList.addAll(importCasesFromCSV(filePath));
    }

    @FXML
    private void onAddCase(){
     openInputWindow(null);
    }
    @FXML
    private void onSave(){
        exportCasesToCSV("data.csv");
        saveLabel.setText("Data saved");
    }
    @FXML
    private void onExport(){
        exportCasesToCSV(null);
    }
    @FXML
    private void onViewChart(){
        openChartWindow();
    }

    private void openChartWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("chart-view.fxml"));
            StackPane newWindowRoot = loader.load();

            // Create an instance of GanttChartRenderer
            ChartController chartRenderer = loader.getController();
            chartRenderer.setMainController(this);
            chartRenderer.setCaseList(caseList);


            // Add the Gantt chart to the loaded FXML root layout
            chartRenderer.createAndSetChart();
            chartRenderer.addToScene(newWindowRoot);
            Stage newWindow = new Stage();
            newWindow.setTitle("Chart");
            newWindow.setScene(new Scene(newWindowRoot, 800, 600)); // Optional: Set preferred size



            newWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openInputWindow(Case selectedItem) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("input-view.fxml"));
            GridPane newWindowRoot = loader.load();

            Stage newWindow = new Stage();
            newWindow.setTitle(selectedItem == null ? "New Case": selectedItem.getCaseId());
            newWindow.setScene(new Scene(newWindowRoot));

            InputController newWindowController = loader.getController();
            newWindowController.setMainController(this);
            newWindowController.setData(selectedItem);

            newWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void onImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            System.out.println("Before clear: " + caseList);
            caseList.clear();
            System.out.println("After clear: " + caseList);
            caseList.add(new Case("1", "Test", "User", "Description", LocalDate.now(), LocalDateTime.now(), "Color", "Material", false));
            System.out.println("After add: " + caseList);

        }
    }

    @FXML
    private void exportCasesToCSV(String filename) {
        Path filePath;
        if (filename == null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Cases to CSV");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = fileChooser.showSaveDialog(null);
            filePath = file != null ? file.toPath() : null;
        } else {
            filePath = Paths.get("src/main/resources/data.csv");
        }

        if (filePath != null) {
            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                writer.write("Case ID,Name,Surname,Description,Start Date,End Date,Tooth Color,Material,Finished");
                writer.newLine();

                for (Case caseObj : caseList) {
                    writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s",
                            caseObj.getCaseId(),
                            caseObj.getName(),
                            caseObj.getSurname(),
                            caseObj.getDescription(),
                            caseObj.getStartDate(),
                            caseObj.getEndDate(),
                            caseObj.getToothColor(),
                            caseObj.getMaterial(),
                            caseObj.isFinished()));
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Case> importCasesFromCSV(Path filePath) {
        List<Case> cases = new ArrayList<>();
        final int fieldsCount = 9;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields.length == fieldsCount) {
                    String caseId = fields[0];
                    String name = fields[1];
                    String surname = fields[2];
                    String description = fields[3];

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate startDate = LocalDate.parse(fields[4], formatter);

                    LocalDateTime endDate = LocalDateTime.parse(fields[5], dateTimeFormatter);
                    String toothColor = fields[6];
                    String material = fields[7];
                    Boolean finished = Boolean.valueOf(fields[8]);

                    Case caseObj = new Case(caseId, name, surname, description, startDate, endDate, toothColor, material, finished);
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
