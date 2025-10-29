module com.meteonow {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.meteonow to javafx.fxml;
    exports com.meteonow;
}
