module com.fxapp.enigmasimulatorfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires org.controlsfx.controls;

    opens com.fxapp.enigmasimulatorfx to javafx.fxml;
    exports com.fxapp.enigmasimulatorfx;
}