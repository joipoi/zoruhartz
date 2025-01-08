module com.example.zoruhartz {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jfree.jfreechart;
    requires org.jfree.chart.fx;
    requires java.sql;


    opens com.example.zoruhartz to javafx.fxml;
    exports com.example.zoruhartz;
}