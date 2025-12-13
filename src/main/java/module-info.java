module gruppocinque.bibliosoft {
    requires javafx.controls;
    requires javafx.fxml;

    opens gruppocinque.bibliosoft.controller to javafx.fxml;
    opens gruppocinque.bibliosoft.modelli to javafx.base;

    exports gruppocinque.bibliosoft;
}