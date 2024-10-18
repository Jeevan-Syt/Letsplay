package com.unisol.letsplay.model;

        public class User {
        private int user_id;
        private String username;
        private String email_id;
        private String password;
        private int phone_number;
        private String user_profilepic;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public int getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(int phone_number) {
        this.phone_number = phone_number;
    }

    public String getUser_profilepic() {
        return user_profilepic;
    }

    public void setUser_profilepic(String user_profilepic) {
        this.user_profilepic = user_profilepic;
    }
}
