module com.example.roomreservationsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.roomreservationsystem to javafx.fxml;
    exports com.example.roomreservationsystem;
    exports com.example.roomreservationsystem.Factory;
    opens com.example.roomreservationsystem.Factory to javafx.fxml;
    exports com.example.roomreservationsystem.State;
    opens com.example.roomreservationsystem.State to javafx.fxml;
    exports com.example.roomreservationsystem.Room;
    opens com.example.roomreservationsystem.Room to javafx.fxml;
}