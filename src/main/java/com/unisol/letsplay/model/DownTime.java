package com.unisol.letsplay.model;

import java.sql.Time;
import java.util.Date;

public class DownTime {

    private int downTimeId;
    private int venue_id;
    private int court_id;
    private Date start_date;
    private Date end_date;
    private Time start_time;
    private Time end_time;
    private boolean repeateMode;

    public int getDownTimeId() {
        return downTimeId;
    }

    public void setDownTimeId(int downTimeId) {
        this.downTimeId = downTimeId;
    }

    public int getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(int venue_id) {
        this.venue_id = venue_id;
    }

    public int getCourt_id() {
        return court_id;
    }

    public void setCourt_id(int court_id) {
        this.court_id = court_id;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Time getStart_time() {
        return start_time;
    }

    public void setStart_time(Time start_time) {
        this.start_time = start_time;
    }

    public Time getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Time end_time) {
        this.end_time = end_time;
    }

    public boolean isRepeateMode() {
        return repeateMode;
    }

    public void setRepeateMode(boolean repeateMode) {
        this.repeateMode = repeateMode;
    }
}
