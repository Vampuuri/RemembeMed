package fi.esupponen.remembemed;

public class Medication {
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
}
