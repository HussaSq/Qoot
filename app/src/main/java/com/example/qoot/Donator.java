package com.example.qoot;

public class Donator {
    private String UserName;
    private String Email;
    private String Gender;
    private String PhoneNumber;
    private String type;


    public Donator(){
        UserName="";
        Email="";
        Gender="";
        PhoneNumber="";

    }

    public Donator(String u,String e,String g){
        UserName=u;
        Email=e;
        Gender=g;
        PhoneNumber = "05xxxxxxxx";
        type="Donator";
    }

    public String getEmail() {
        return Email;
    }

    public String getGender() {
        return Gender;
    }

    public String getUserName() {
        return UserName;
    }

    public String getPhone() {
        return PhoneNumber;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public void setPhone(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }
}
