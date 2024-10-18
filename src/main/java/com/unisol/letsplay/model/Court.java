package com.unisol.letsplay.model;

import java.sql.Timestamp;

public class Court {
    private int court_id;
    private String court_name;
    private String court_img;
    private int venue_id;           ////foreign key of venue
    private String other_details;
    private double price;
    private String game_type;
    private Timestamp open_time;
    private Timestamp close_time;
    private String closed_days;
    private boolean operational_status;

    public int getCourt_id() {
        return court_id;
    }

    public void setCourt_id(int court_id) {
        this.court_id = court_id;
    }

    public String getCourt_name() {
        return court_name;
    }

    public void setCourt_name(String court_name) {
        this.court_name = court_name;
    }

    public String getCourt_img() {
        return court_img;
    }

    public void setCourt_img(String court_img) {
        this.court_img = court_img;
    }

    public int getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(int venue_id) {
        this.venue_id = venue_id;
    }

    public String getOther_details() {
        return other_details;
    }

    public void setOther_details(String other_details) {
        this.other_details = other_details;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getGame_type() {
        return game_type;
    }

    public void setGame_type(String game_type) {
        this.game_type = game_type;
    }

    public Timestamp getOpen_time() {
        return open_time;
    }

    public void setOpen_time(Timestamp open_time) {
        this.open_time = open_time;
    }

    public Timestamp getClose_time() {
        return close_time;
    }

    public void setClose_time(Timestamp close_time) {
        this.close_time = close_time;
    }

    public String getClosed_days() {
        return closed_days;
    }

    public void setClosed_days(String closed_days) {
        this.closed_days = closed_days;
    }

    public boolean isOperational_status() {
        return operational_status;
    }

    public void setOperational_status(boolean operational_status) {
        this.operational_status = operational_status;
    }
}
