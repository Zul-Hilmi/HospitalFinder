package com.example.hospitalfinder;

public class User {

    String userName;
    String userLocation;

    public User(){}

    public User(String userName) {
        this.userName = userName;
        this.userLocation = "Unset";
    }

    public String getUserName() {return userName;}
    public String getUserLocation(){return userLocation;}
    public void setUserLocation(String location){
        if(location!=null && location!="Unset"){
            userLocation=location;
        }
    }
}
