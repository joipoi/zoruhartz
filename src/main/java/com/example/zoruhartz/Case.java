package com.example.zoruhartz;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

public class Case {
    private final StringProperty caseId;
    private final StringProperty name;
    private final StringProperty surname;
    private final StringProperty description;
    private final StringProperty toothColor;
    private final StringProperty material;

    private final ObjectProperty<LocalDateTime> receiptDate;

    // Constructor
    public Case(String caseId, String name, String surname, String description,
                LocalDateTime receiptDate, String toothColor, String material) {
        this.caseId = new SimpleStringProperty(caseId);
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.description = new SimpleStringProperty(description);
        this.receiptDate = new SimpleObjectProperty(receiptDate);
        this.toothColor = new SimpleStringProperty(toothColor);
        this.material = new SimpleStringProperty(material);
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


    public void setMaterial(String material) {
        this.material.set(material);
    }

    public LocalDateTime getReceiptDate() {
        return receiptDate.get();
    }

    public void setReceiptDate(LocalDateTime receiptDate) {
        this.receiptDate.set(receiptDate);
    }

    @Override
    public String toString() {
        return "id: " + this.getCaseId() + " Name: " + this.getName() + this.getSurname() + " Date: " + this.getReceiptDate();
    }

   /* @Override
    public String toString() {
        return "Case{" +
                "caseId='" + caseId + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", description='" + description + '\'' +
                ", receiptDate=" + receiptDate +
                ", toothColor='" + toothColor + '\'' +
                ", material='" + material + '\'' +
                '}';
    } */
}
