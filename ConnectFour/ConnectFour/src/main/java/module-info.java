module com.connectfour.connectfour {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.connectfour.connectfour to javafx.fxml;
    exports com.connectfour.connectfour;
}