package fi.esupponen.remembemed.classes;

import java.io.Serializable;
import java.util.LinkedList;

import fi.esupponen.remembemed.classes.Alarm;

public class Medication implements Serializable {
    String name;
    LinkedList<Alarm> alarms;

    public Medication(String name) {
        this.name = name;
        this.alarms = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<Alarm> getAlarms() {
        return alarms;
    }
}
