package com.example.qoot;

public class Volunteer extends Object {

    private String UserName;
    private String Name;
    private String PhoneNumber;
    private String Password;
    private String Email;
    private String Gender;
    private String Vehicle;

    public Volunteer(){
        UserName="";
        Name="";
        Password="";
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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
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
        this.Name=userName.trim();
        this.Password=password;
    }




}
