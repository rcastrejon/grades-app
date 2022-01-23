package dev.rcastrejon.gradesapp;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.Optional;

public class StudentOverviewController {
    // Table properties
    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, String> registrationNumberColumn;
    @FXML
    private TableColumn<Student, String> firstSurnameColumn;
    @FXML
    private TableColumn<Student, String> secondSurnameColumn;
    @FXML
    private TableColumn<Student, String> namesColumn;
    // Grades pane
    @FXML
    private AnchorPane gradesPane;
    @FXML
    private TextField grade1Field;
    // Other
    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        studentTable.setItems(mainApp.getStudentData());
    }

    @FXML
    private void initialize(){
        // Inicializar las columnas de la tabla
        registrationNumberColumn.setCellValueFactory(cellData->cellData.getValue().registrationNumberProperty());
        firstSurnameColumn.setCellValueFactory(cellData->cellData.getValue().firstSurnameProperty());
        secondSurnameColumn.setCellValueFactory(cellData->cellData.getValue().secondSurnameProperty());
        namesColumn.setCellValueFactory(cellData->cellData.getValue().namesProperty());

        // Desactivar el panel de calificaciones
        showStudentGrades(null);

        // Escuchar cambios del estudiante seleccionado de la tabla para actualizar calificaciones
        studentTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue)-> showStudentGrades(newValue)
        );
    }

    @FXML
    private void handleSave(){
        // Solo continuar si el textField de la calificación es válido
        if (isInputValid()){
            int selectedIndex = studentTable.getSelectionModel().getSelectedIndex();
            Student updatedStudent = studentTable.getSelectionModel().getSelectedItem();
            updatedStudent.setGrade1(Optional.of(Integer.parseInt(grade1Field.getText())));
            mainApp.getStudentData().set(selectedIndex,updatedStudent);

            // Verificar que todos los alumnos tengan una calificación asignada, si ese es el caso,
            // cambiar la propiedad allStudentsComplete a verdadero, lo que hará que el botón de
            // "Generar CSV" se active.
            boolean allStudentsComplete = true;
            for(Student student: mainApp.getStudentData()){
                if (student.getGrade1().isEmpty()) {
                    allStudentsComplete = false;
                    break;
                }
            }
            if (allStudentsComplete) mainApp.setAllStudentsComplete(true);
        }
    }

    private void showStudentGrades(Student student){
        if (student == null){
            gradesPane.setDisable(true);
            grade1Field.setText("");
        } else {
            if (gradesPane.isDisable()) gradesPane.setDisable(false);
            student.getGrade1().ifPresentOrElse(
                    grade -> {grade1Field.setText(String.valueOf(grade));},
                    () -> {grade1Field.setText("");});
        }
    }

    private boolean isInputValid(){
        String errorMessage = "";

        if (grade1Field.getText().isBlank()){
            errorMessage += "Introduzca una calificación.\n";
        } else {
            // Intentar convertir el texto a un número y verificar que esté en escala del 1 al 100
            try {
                int value = Integer.parseInt(grade1Field.getText());
                if (value < 1 || value > 100){
                    errorMessage += "La calificación no es válida (debe de estar en el rango del 1 al 100)\n";
                }
            } catch (NumberFormatException e){
                errorMessage += "La calificación no es válida (debe de ser un número entero).\n";
            }
        }

        if (errorMessage.isEmpty()) {
            return true;
        }

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Error");
        alert.setHeaderText("No se puedo guardar la calificación.");
        alert.setContentText(errorMessage);
        alert.showAndWait();
        return false;
    }
}
