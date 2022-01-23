package dev.rcastrejon.gradesapp;

import javafx.beans.property.*;

import java.util.Optional;

public class Student {
    private final StringProperty registrationNumber;
    private final StringProperty firstSurname;
    private final StringProperty secondSurname;
    private final StringProperty names;
    private final ObjectProperty<Optional<Integer>> grade1;

    public Student(String registrationNumber, String firstSurname, String secondSurname, String names) {
        this.registrationNumber = new SimpleStringProperty(registrationNumber);
        this.firstSurname = new SimpleStringProperty(firstSurname);
        this.secondSurname = new SimpleStringProperty(secondSurname);
        this.names = new SimpleStringProperty(names);

        this.grade1 = new SimpleObjectProperty<>(Optional.empty());
    }

    public String getRegistrationNumber() {
        return registrationNumber.get();
    }

    public StringProperty registrationNumberProperty() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber.set(registrationNumber);
    }

    public String getFirstSurname() {
        return firstSurname.get();
    }

    public StringProperty firstSurnameProperty() {
        return firstSurname;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname.set(firstSurname);
    }

    public String getSecondSurname() {
        return secondSurname.get();
    }

    public StringProperty secondSurnameProperty() {
        return secondSurname;
    }

    public void setSecondSurname(String secondSurname) {
        this.secondSurname.set(secondSurname);
    }

    public String getNames() {
        return names.get();
    }

    public StringProperty namesProperty() {
        return names;
    }

    public void setNames(String names) {
        this.names.set(names);
    }

    public Optional<Integer> getGrade1() {
        return grade1.get();
    }

    public ObjectProperty<Optional<Integer>> grade1Property() {
        return grade1;
    }

    public void setGrade1(Optional<Integer> grade1) {
        this.grade1.set(grade1);
    }
}
