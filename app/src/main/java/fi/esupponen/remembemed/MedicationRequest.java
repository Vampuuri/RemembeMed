package fi.esupponen.remembemed;

import java.io.Serializable;

public enum MedicationRequest implements Serializable {
    CHANGE_NAME, DELETE, UPDATE, ADD_ALARM, REMOVE_ALARM, SET_ALARM_ACTIVE, SET_TAKEN
}
