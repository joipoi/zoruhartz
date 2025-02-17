module com.example.zoruhartz {
    requires javafx.controls;
    requires javafx.fxml;
   // requires org.jfree.jfreechart;
   // requires org.jfree.chart.fx;
    requires java.sql;
    requires java.desktop;
    requires org.apache.commons.csv;


    opens com.example.zoruhartz to javafx.fxml;
    exports com.example.zoruhartz;
}