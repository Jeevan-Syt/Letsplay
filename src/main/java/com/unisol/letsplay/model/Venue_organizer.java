package com.unisol.letsplay.model;

public class Venue_organizer {
    private int organizer_id;
    private String username;
    private String email_id;
    private String password;
    private String user_profilepic;
    private String org_flname;

    public int getOrganizer_id() {
        return organizer_id;
    }

    public void setOrganizer_id(int organizer_id) {
        this.organizer_id = organizer_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_profilepic() {
        return user_profilepic;
    }

    public void setUser_profilepic(String user_profilepic) {
        this.user_profilepic = user_profilepic;
    }

    public String getOrg_flname() {
        return org_flname;
    }

    public void setOrg_flname(String org_flname) {
        this.org_flname = org_flname;
    }
}
