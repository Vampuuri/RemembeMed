package fi.esupponen.remembemed;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class Medication implements Serializable {
    String name;
    String dose;
    LinkedList<Alarm> alarms;

    public Medication(String name, String dose) {
        this.name = name;
        this.dose = dose;
        this.alarms = new LinkedList<>();
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

    public LinkedList<Alarm> getAlarms() {
        return alarms;
    }
}
