package com.example.zoruhartz;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainController {
    @FXML
    private Label label;
    @FXML
    private ListView<Case> listView;


    private static ObservableList<Case> caseList = FXCollections.observableArrayList();

    public static void addCase(Case newCase) {
        caseList.add(newCase); // Add a new case
    }

    public static void removeCase(Case caseToRemove) {
        caseList.remove(caseToRemove); // Remove a case
    }

    private Case getCaseById(String caseId) {
        for (Case caseItem : caseList) {
            if (caseItem.getCaseId().equals(caseId)) {
                return caseItem; // Return the existing case
            }
        }
        return null; // No case found with the specified ID
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
                    setText(item.getCaseId()+ item.getName() + item.getSurname()); // Display the case information
                }
            }
        });

        // Handle selection of items
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                openNewWindow(newSelection, false); // Pass the selected item to the new window
            }
        });

    }

    @FXML
    private void onAddCase(){
       // Case test = new Case("123", "joey", "smith", "test", LocalDateTime.now(), "blue", "vinyl");
     openNewWindow(null, true);
    }
    private void openNewWindow(Case selectedItem, boolean isNewCase) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("input-view.fxml"));
            GridPane newWindowRoot = loader.load();

            Stage newWindow = new Stage();
            newWindow.setTitle("New Window");
            newWindow.setScene(new Scene(newWindowRoot));

            InputController newWindowController = loader.getController();
            newWindowController.setData(isNewCase ? null : selectedItem, isNewCase);

            newWindow.setOnCloseRequest(event -> {
                System.out.println("Window is closing! Perform cleanup or save state.");
            });

            newWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  void updateListView() {
        listView.refresh();
    }

}
