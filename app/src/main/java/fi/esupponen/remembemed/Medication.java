package fi.esupponen.remembemed;

import java.io.Serializable;

public class Medication implements Serializable {
    String name;
    String dose;

    public Medication(String name, String dose) {
        this.name = name;
        this.dose = dose;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String toString() {
        return "Medication: " + name + " " + dose;
    }
}
