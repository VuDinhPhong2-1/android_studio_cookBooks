package com.example.doanltttbdd.model;


public class User {

    private String email;
    private String fullName;
    private String password;

    private String role;
    private String phone;
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String fullName, String email, String phone, String password) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.phone = phone;
        this.role = "user";
    }

    public User() {

    }


}
