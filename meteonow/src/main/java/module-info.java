module com.meteonow {
    requires com.google.gson;
    requires java.net.http;
    requires javafx.controls;
    requires javafx.fxml;

    opens com.meteonow to javafx.fxml;
    exports com.meteonow;
}
