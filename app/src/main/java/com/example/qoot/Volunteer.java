package com.example.qoot;

public class Volunteer extends Object {

    private String UserName;
    private String PhoneNumber;
    private String Email;
    private String Gender;
    private String Vehicle;

    public Volunteer(){
        UserName="";
        PhoneNumber="";
        Email="";
        Gender="";
        Vehicle="";
    }


    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }



    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getVehicle() {
        return Vehicle;
    }

    public void setVehicle(String vehicle) {
        Vehicle = vehicle;
    }

    public Volunteer(String userName , String Email, String password, String Vehicle , String Gender){
        this.UserName=userName;
        this.Email= Email;
        this.Gender=Gender;
        this.Vehicle=Vehicle;
        this.PhoneNumber="05xxxxxxxx";
    }




}
