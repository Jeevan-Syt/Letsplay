package com.unisol.letsplay.model;

public class Venue {
    private int venue_id;
    private String organizer_id; //foreign key of venue_ogranizer
    private String venue_address;
    private String location;
    private String picture_url;
    private int phone_number;
    private String operational_status;

    public int getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(int venue_id) {
        this.venue_id = venue_id;
    }

    public String getOrganizer_id() {
        return organizer_id;
    }

    public void setOrganizer_id(String organizer_id) {
        this.organizer_id = organizer_id;
    }

    public String getVenue_address() {
        return venue_address;
    }

    public void setVenue_address(String venue_address) {
        this.venue_address = venue_address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public int getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(int phone_number) {
        this.phone_number = phone_number;
    }

    public String getOperational_status() {
        return operational_status;
    }

    public void setOperational_status(String operational_status) {
        this.operational_status = operational_status;
    }
}