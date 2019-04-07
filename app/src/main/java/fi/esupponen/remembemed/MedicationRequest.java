package fi.esupponen.remembemed;

import java.io.Serializable;

public enum MedicationRequest implements Serializable {
    CHANGE_NAME, CHANGE_DOSE, DELETE, UPDATE, ADD_ALARM
}
