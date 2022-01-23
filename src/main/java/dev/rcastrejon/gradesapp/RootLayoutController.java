package dev.rcastrejon.gradesapp;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class RootLayoutController {
    // Buttons
    @FXML
    private Button loadButton;
    @FXML
    private Button generateButton;
    // Other
    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        // Habilitar el botón de "Generar CSV" cuando todos los alumnos tengan una calificación asignada
        generateButton.disableProperty().bind(Bindings.createBooleanBinding(() -> !this.mainApp.areAllStudentsComplete(), this.mainApp.allStudentsCompleteProperty()));
    }

    @FXML
    private void initialize() {
        generateButton.setDisable(true);
    }

    @FXML
    private void handleLoad() {
        // Si se carga un archivo satisfactoriamente, desactivar el botón de "Cargar CSV"
        try{
            mainApp.loadStudentsFromFile("alumnos.csv");
            loadButton.setDisable(true);
        }catch (StudentFileException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo cargar el archivo.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleGenerate() {
        try{
            mainApp.saveStudentsToFile("calificaciones.csv");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Confirmación");
            alert.setHeaderText("El archivo se generó satisfactoriamente.");
            alert.showAndWait();
            mainApp.setHasFileBeenGenerated(true);
        } catch (StudentFileException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
