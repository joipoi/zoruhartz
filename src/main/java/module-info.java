module com.example.zoruhartz {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.zoruhartz to javafx.fxml;
    exports com.example.zoruhartz;
}