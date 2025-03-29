package com.unisol.letsplay.model;

import java.sql.Timestamp;

public class Court {
    private int court_id;
    private String court_name;
    private String court_img;
    private int venue_id;
    private String other_details;
    private double price;
    private String gameType;
    private Timestamp openTime;
    private Timestamp closeTime;
    private String closedDays;
    private boolean operational_status;

    // Getters and Setters (same as before)
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

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public Timestamp getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Timestamp openTime) {
        this.openTime = openTime;
    }

    public Timestamp getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Timestamp closeTime) {
        this.closeTime = closeTime;
    }

    public String getClosedDays() { //TODO:- it should be closed day of the week
        return closedDays;
    }

    public void setClosedDays(String closedDays) {
        this.closedDays = closedDays;
    }

    public boolean isOperational_status() {
        return operational_status;
    }

    public void setOperational_status(boolean operational_status) {
        this.operational_status = operational_status;
    }
}