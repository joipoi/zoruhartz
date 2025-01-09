package com.example.zoruhartz;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Case {
    private final StringProperty caseId;
    private final StringProperty name;
    private final StringProperty surname;
    private final StringProperty description;
    private final StringProperty toothColor;
    private final StringProperty material;
    private final ObjectProperty<LocalDate> startDate;
    private final ObjectProperty<LocalDateTime> endDate;
    private final BooleanProperty finished;


    // Constructor
    public Case(String caseId, String name, String surname, String description, LocalDate startDate,
                LocalDateTime endDate, String toothColor, String material, boolean finished) {
        this.caseId = new SimpleStringProperty(caseId);
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.description = new SimpleStringProperty(description);
        this.startDate = new SimpleObjectProperty<>(startDate);
        this.endDate = new SimpleObjectProperty(endDate);
        this.toothColor = new SimpleStringProperty(toothColor);
        this.material = new SimpleStringProperty(material);
        this.finished = new SimpleBooleanProperty(finished);
    }

    // Getters and Setters
    public String getCaseId() {
        return caseId.get();
    }

    public void setCaseId(String value) {
        caseId.set(value);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSurname() {
        return surname.get();
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getToothColor() {
        return toothColor.get();
    }


    public void setToothColor(String toothColor) {
        this.toothColor.set(toothColor);
    }

    public String getMaterial() {
        return material.get();
    }

    public boolean isFinished() {
        return finished.get();
    }
    public void setFinished(Boolean finished) {
        this.finished.set(finished);
    }

    public void setMaterial(String material) {
        this.material.set(material);
    }

    public LocalDateTime getEndDate() {
        return endDate.get();
    }
    public String getEndTime() {
        LocalDateTime _endDate = endDate.get();
        if (_endDate != null) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            return _endDate.format(timeFormatter);
        }
        return null; // or return an empty string, depending on your preferenceSimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");

    }
    public LocalDate getStartDate() {
        return startDate.get();
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate.set(startDate);
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate.set(endDate);
    }


    @Override
    public String toString() {
        return "id: " + this.getCaseId() + " Name: " + this.getName() + this.getSurname() + " Date: " + this.getEndDate();
    }


    public String toStringLong() {
        return  "caseId='" + this.getCaseId() + "\n" +
                "name='" + this.getName() + ' ' + this.getSurname() +  "\n" +
                "description='" + this.getDescription() + "\n" +
                "StartDate=" + this.getStartDate() +"\n"+
                "EndDate='" + this.getEndDate() + "\n" +
                "toothColor='" + this.getToothColor() +"\n" +
                "material='" + this.getMaterial() +"\n" +
                "Finished='" + this.isFinished();
    }
}
