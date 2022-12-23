package com.krealif.beritaku.model;

public class User {
    private String username, password, dob;

    public User() {
    }

    public User(String username, String password, String dob) {
        this.username = username;
        this.password = password;
        this.dob = dob;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
