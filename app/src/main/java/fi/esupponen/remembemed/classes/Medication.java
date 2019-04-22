package fi.esupponen.remembemed.classes;

import java.io.Serializable;
import java.util.LinkedList;

import fi.esupponen.remembemed.classes.Alarm;

public class Medication implements Serializable {

    /**
     * Name of the medication.
     */
    String name;

    /**
     * List of all alarms medication has.
     */
    LinkedList<Alarm> alarms;

    /**
     * Basic constructor.
     *
     * @param name
     */
    public Medication(String name) {
        this.name = name;
        this.alarms = new LinkedList<>();
    }

    /**
     * Returns name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets given name.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return list of all alarms.
     *
     * @return alarms of medication.
     */
    public LinkedList<Alarm> getAlarms() {
        return alarms;
    }
}
