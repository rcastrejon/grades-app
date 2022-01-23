module dev.rcastrejon.gradesapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens dev.rcastrejon.gradesapp to javafx.fxml;
    exports dev.rcastrejon.gradesapp;
}