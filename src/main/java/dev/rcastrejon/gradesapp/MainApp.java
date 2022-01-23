package dev.rcastrejon.gradesapp;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainApp extends Application {
    private Stage primaryStage;

    private final ObservableList<Student> studentData = FXCollections.observableArrayList();
    private final BooleanProperty allStudentsComplete = new SimpleBooleanProperty(false);
    private boolean hasFileBeenGenerated = false;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public ObservableList<Student> getStudentData() {
        return studentData;
    }

    public boolean areAllStudentsComplete() {
        return allStudentsComplete.get();
    }

    public BooleanProperty allStudentsCompleteProperty() {
        return allStudentsComplete;
    }

    public void setAllStudentsComplete(boolean allStudentsComplete) {
        this.allStudentsComplete.set(allStudentsComplete);
    }

    public void setHasFileBeenGenerated(boolean hasFileBeenGenerated) {
        this.hasFileBeenGenerated = hasFileBeenGenerated;
    }

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        // Cargar RootLayout y agregarlo al primaryStage
        FXMLLoader rlLoader = new FXMLLoader(MainApp.class.getResource("RootLayout.fxml"));
        BorderPane rootLayout = rlLoader.load();
        Scene scene = new Scene(rootLayout);
        primaryStage.setTitle("Calificaciones");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Cargar StudentOverview y agregarlo al centro de rootLayout
        FXMLLoader soLoader = new FXMLLoader(MainApp.class.getResource("StudentOverview.fxml"));
        rootLayout.setCenter(soLoader.load());

        // Dar acceso a los controladores
        RootLayoutController rlController = rlLoader.getController();
        rlController.setMainApp(this);

        StudentOverviewController soController = soLoader.getController();
        soController.setMainApp(this);

        // Si el usuario ha capturado todas las calificaciones, no ha generado el archivo csv y quiere salir de la
        // aplicación, mandar una notificación para confirmar su decisión.
        primaryStage.setOnCloseRequest(windowEvent -> {
            if (!allStudentsComplete.get() || hasFileBeenGenerated) return;
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alerta");
            alert.setHeaderText("¿Desea continuar?");
            alert.setContentText("¿Está seguro de salir de la aplicación, no se ha generado el archivo de calificaciones?");
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType != ButtonType.OK) windowEvent.consume();
            });
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    // https://www.baeldung.com/java-csv-file-array
    public void loadStudentsFromFile(String file) throws StudentFileException {
        studentData.clear();

        boolean isNotEmpty = false;
        List<Student> records = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while((line = br.readLine()) != null){
                isNotEmpty = true;
                String[] values = line.split(",");
                if (values.length != 4) {
                    throw new StudentFileException("El archivo no cuenta con las columnas esperadas.\n");
                }
                Student student = new Student(values[0], values[1],values[2], values[3]);
                records.add(student);
            }
        } catch (FileNotFoundException e) {
            throw new StudentFileException(String.format("No se encontró el archivo esperado (%s).\n",file));
        } catch (IOException e) {
            e.printStackTrace();
            throw new StudentFileException(e.getMessage());
        }

        if(!isNotEmpty) {
            throw new StudentFileException("El archivo proporcionado está vacío.\n");
        }

        studentData.addAll(records);
    }

    // https://www.baeldung.com/java-csv
    public void saveStudentsToFile(String file) throws StudentFileException{
        File csvOutput = new File(file);
        try (PrintWriter pw =new PrintWriter(csvOutput)){
            studentData.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new StudentFileException(e.getMessage());
        }
        if (!csvOutput.exists()) throw new StudentFileException("El archivo no se creó correctamente.\n");
    }

    public String convertToCSV(Student data){
        String[] values = new String[]{data.getRegistrationNumber(), "Diseño de Software", String.valueOf(data.getGrade1().get())};
        return String.join(",", values);
    }
}
