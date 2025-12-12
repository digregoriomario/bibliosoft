module gruppo5.bibliosoft {
    requires javafx.controls;
    requires javafx.fxml;

    opens gruppo5.bibliosoft.controller to javafx.fxml;
    opens gruppo5.bibliosoft.modelli to javafx.base;

    exports gruppo5.bibliosoft;
}