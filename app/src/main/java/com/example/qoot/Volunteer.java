package com.example.qoot;

public class Volunteer extends Object {

    private String username;
    private String phoneNumber;
    private String email;
    private String gender;
    private String vehicle;
    private String type;

    public Volunteer(){
        username ="";
        phoneNumber ="";
        email ="";
        gender ="";
        vehicle ="";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public Volunteer(String userName , String Email, String password, String Vehicle , String Gender){
        this.username =userName;
        this.email = Email;
        this.gender =Gender;
        this.vehicle =Vehicle;
        this.phoneNumber ="05xxxxxxxx";
        this.type = "Volunteer";
    }




}
