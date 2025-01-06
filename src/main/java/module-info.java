module com.example.zoruhartz {
    requires javafx.controls;
    requires javafx.fxml;
    requires ical4j.core;


    opens com.example.zoruhartz to javafx.fxml;
    exports com.example.zoruhartz;
}