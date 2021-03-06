package com.stemi.stemiapp.model;

import java.util.Date;

/**
 * Created by praburaam on 04/09/17.
 */

public class TrackMedication {
    private String userId;
    private Date dateTime;
    private boolean hadAllMedicines;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isHadAllMedicines() {
        return hadAllMedicines;
    }

    public void setHadAllMedicines(boolean hadAllMedicines) {
        this.hadAllMedicines = hadAllMedicines;
    }
}
