package com.example.zoruhartz;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.util.Arrays;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.example.zoruhartz.GanttChart.ExtraData;

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
                    GridPane grid = new GridPane();
                    grid.setHgap(10);

                    Text caseIdText = new Text("ID: " + item.getCaseId());
                    Text nameText = new Text("Name: " + item.getName() +" "+ item.getSurname());
                    Text endDateText = new Text("End Date: " + item.getEndDateString());
                    Text finished = new Text(item.isFinished() ? "Finished" : "Not Finished");

                    // Add columns with specific constraints
                    ColumnConstraints col1 = new ColumnConstraints();
                    col1.setPrefWidth(100);
                    ColumnConstraints col2 = new ColumnConstraints();
                    col2.setPrefWidth(200);
                    ColumnConstraints col3 = new ColumnConstraints();
                    col3.setPrefWidth(150);
                    grid.getColumnConstraints().addAll(col1, col2, col3);

                    grid.add(caseIdText, 0, 0);
                    grid.add(nameText, 1, 0);
                    grid.add(endDateText, 2, 0);
                    grid.add(finished, 3, 0);

                    setGraphic(grid);

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
    private void openChartWindow(){
            final NumberAxis xAxis = new NumberAxis();
            final CategoryAxis yAxis = new CategoryAxis();

            final GanttChart<Number,String> chart = new GanttChart<Number,String>(xAxis,yAxis);

        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);

        yAxis.setLabel("");
            yAxis.setTickLabelFill(Color.CHOCOLATE);
            yAxis.setTickLabelGap(10);

            chart.setTitle("Cases");
            chart.setLegendVisible(false);
            chart.setBlockHeight( 50);

            LocalDate[] dateRange = getDateRange();
            LocalDate startDate= dateRange[0];

            for (Case caseItem : caseList) {
                LocalDate caseStart = caseItem.getStartDate();
                LocalDate caseEnd = caseItem.getEndDate().toLocalDate();

                int start = (int) ChronoUnit.DAYS.between(startDate, caseStart);
                int length = (int) ChronoUnit.DAYS.between(caseStart, caseEnd);
                String style;
                if(caseItem.isFinished()){
                    style="status-green";
                }else{
                    style="status-red";
                }

                XYChart.Series caseSeries = new XYChart.Series();
                caseSeries.getData().add(new XYChart.Data(start, caseItem.getCaseId(), new ExtraData( length, style)));

                chart.getData().add(caseSeries);
            }

        chart.getStylesheets().add(getClass().getResource("ganttchart.css").toExternalForm());

        Stage newWindow = new Stage();
            newWindow.setTitle("Chart");
            newWindow.setScene(new Scene(chart,1000,800));
            newWindow.show();
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
            Path filePath = Paths.get(file.getPath());
            caseList.addAll(importCasesFromCSV(filePath));
            System.out.println("After add: " + caseList);

        }
    }

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
            try (BufferedWriter writer = Files.newBufferedWriter(filePath);
                 CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Case ID", "Name", "Surname", "Description", "Start Date", "End Date", "Tooth Color", "Material", "Finished"))) {

                for (Case caseObj : caseList) {
                    csvPrinter.printRecord(
                            caseObj.getCaseId(),
                            caseObj.getName(),
                            caseObj.getSurname(),
                            caseObj.getDescription(),
                            caseObj.getStartDate(),
                            caseObj.getEndDate(),
                            caseObj.getToothColor(),
                            caseObj.getMaterial(),
                            caseObj.isFinished()
                    );
                }

                csvPrinter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Case> importCasesFromCSV(Path filePath) {
        List<Case> cases = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        try (BufferedReader reader = Files.newBufferedReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord csvRecord : csvParser) {
                String caseId = csvRecord.get("Case ID");
                String name = csvRecord.get("Name");
                String surname = csvRecord.get("Surname");
                String description = csvRecord.get("Description").replace("\"", "");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate startDate = LocalDate.parse(csvRecord.get("Start Date"), formatter);
                LocalDateTime endDate = LocalDateTime.parse(csvRecord.get("End Date"), dateTimeFormatter);
                String toothColor = csvRecord.get("Tooth Color");
                String material = csvRecord.get("Material");
                Boolean finished = Boolean.valueOf(csvRecord.get("Finished"));

                Case caseObj = new Case(caseId, name, surname, description, startDate, endDate, toothColor, material, finished);
                cases.add(caseObj);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cases;
    }
    private LocalDate[] getDateRange() {
        LocalDate minStartDate = null;
        LocalDateTime maxEndDate = null;

        for (Case caseItem : caseList) {
            LocalDate caseStart = caseItem.getStartDate();
            LocalDateTime caseEnd = caseItem.getEndDate();

            if (minStartDate == null || caseStart.isBefore(minStartDate)) {
                minStartDate = caseStart;
            }

            if (maxEndDate == null || caseEnd.isAfter(maxEndDate)) {
                maxEndDate = caseEnd;
            }
        }

        return new LocalDate[]{minStartDate, maxEndDate.toLocalDate()};
    }


}
