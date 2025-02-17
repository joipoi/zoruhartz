/*package com.example.zoruhartz;


import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.data.gantt.*;


import java.awt.*;
import java.util.Date;
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
        TaskSeries finishedSeries = new TaskSeries("finished");
        TaskSeries ongoingSeries = new TaskSeries("ongoing");

        for (Case caseItem : caseList) {
            String description = caseItem.getCaseId();
            Date startDate =  Date.from(caseItem.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(caseItem.getEndDate().atZone(ZoneId.systemDefault()).toInstant());
            Task caseTask = new Task(description, startDate, endDate);
            if(caseItem.isFinished()){
                finishedSeries.add(caseTask);
            }else{
                ongoingSeries.add(caseTask);
            }

        }
        dataset.add(finishedSeries);
        dataset.add(ongoingSeries);
        return dataset;
    }

    private JFreeChart createChart(GanttCategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createGanttChart(
                "Cases", // Title
                "CaseId",       // X-Axis Label
                "Date",        // Y-Axis Label
                dataset,       // Dataset
                true,          // Show legend
                true,          // Tooltips
                true          // URLs
        );
        GanttRenderer renderer = new GanttRenderer();
        renderer.setSeriesPaint(0, new Color(34, 130, 59));
        renderer.setSeriesPaint(1, new Color(217, 20, 56));
        renderer.setShadowVisible(false);


        CategoryPlot plot = chart.getCategoryPlot();

       /* renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(1.0f));
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
          chart.setBackgroundPaint(new Color(34, 130, 59));
        chart.setTitle(new TextTitle("Custom Gantt Chart", new Font("Arial", Font.BOLD, 16)));
        chart.setPadding(new RectangleInsets());
        chart.addSubtitle(new TextTitle("Time to generate 1000 charts in SVG "
                + "format (lower bars = better performance)"));
        renderer.setDefaultItemLabelsVisible(true);

        // Create the item label generator
        IntervalCategoryItemLabelGenerator itemLabelGenerator = new IntervalCategoryItemLabelGenerator() {
            @Override
            public String generateLabel(CategoryDataset dataset, int row, int column) {
                // Customize the label text here
                return "Task: " + dataset.getRowKey(row).toString();
            }
        };

// Set the item label generator
        renderer.setDefaultItemLabelGenerator(itemLabelGenerator);
        renderer.setDefaultItemLabelFont(new Font("Arial", Font.BOLD, 12)); // Set font for item labels


        renderer.setDefaultToolTipGenerator((dataset1, row, column) -> caseList[column].toStringLong());

        plot.setRenderer(renderer);

        return chart;
    }

    public void addToScene(StackPane root) {
        root.getChildren().add(chartViewer);
    }


} */
