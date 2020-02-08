package com.example.qoot;

public class Donator {
    String username,email,gender,phone;


    public Donator(){
        username="";
        email="";
        gender="";
        phone="";

    }

    public Donator(String u,String e,String g){
        username=u;
        email=e;
        gender=g;
        phone = "0500000000";
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
