package com.example.zoruhartz;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.data.gantt.*;


import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;

public class ChartController {
    private MainController mainController;
    private Case[] caseList;
    private ChartViewer chartViewer;


    public void setCaseList(ObservableList<Case> caseList) {
        Case[] caseArray = new Case[caseList.size()];
        this.caseList = caseList.toArray(caseArray);
        System.out.println(this.caseList);
    }

    public void setMainController(MainController controller) {
        this.mainController = controller;
    }

    public ChartController() {
    }

    public void createAndSetChart() {
        GanttCategoryDataset underlyingDataset = createDataset();
        JFreeChart chart = createChart(underlyingDataset);
        chartViewer = new ChartViewer(chart);
    }

    private GanttCategoryDataset createDataset() {
        TaskSeriesCollection dataset = new TaskSeriesCollection();
        TaskSeries ts = new TaskSeries("example");

        for (Case caseItem : caseList) {
            String description = caseItem.getCaseId();
            Date startDate =  Date.from(caseItem.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(caseItem.getEndDate().atZone(ZoneId.systemDefault()).toInstant());
            Task caseTask = new Task(description, startDate, endDate);
            ts.add(caseTask);
        }
        dataset.add(ts);
        return dataset;
    }

    private JFreeChart createChart(GanttCategoryDataset dataset) {
        return ChartFactory.createGanttChart(
                "Cases", // Title
                "CaseId",       // X-Axis Label
                "Date",        // Y-Axis Label
                dataset,       // Dataset
                true,          // Show legend
                true,          // Tooltips
                true          // URLs
        );
    }

    public void addToScene(StackPane root) {
        root.getChildren().add(chartViewer);
    }


}
