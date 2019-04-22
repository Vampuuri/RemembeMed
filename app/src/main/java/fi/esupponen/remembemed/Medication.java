package fi.esupponen.remembemed;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

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
}
