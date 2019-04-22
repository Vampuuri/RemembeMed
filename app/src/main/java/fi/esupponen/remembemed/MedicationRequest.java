package fi.esupponen.remembemed;

import java.io.Serializable;

/**
 * @author Essi Supponen [essi.supponen@tuni.fi]
 * @version 2019-04-23
 * @since 1.8
 */
public enum MedicationRequest implements Serializable {
    CHANGE_NAME, DELETE, UPDATE, ADD_ALARM, REMOVE_ALARM, SET_ALARM_ACTIVE, SET_TAKEN
}
