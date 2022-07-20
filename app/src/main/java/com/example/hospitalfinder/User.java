package com.example.hospitalfinder;

public class User {

    String userName;
    String userLocation;
    String userCoordinate;
    String insertedAt;

    public User(){}

    public User(String userName) {
        this.userName = userName;
        this.userLocation = "Unset";
        this.userCoordinate = "Unset";
        this.insertedAt="Unset";
    }

    public String getUserName() {return userName;}
    public String getUserLocation(){return userLocation;}
    public String getUserCoordinate(){return userLocation;}

    public void setUserLocation(String location){
        if(location!=null && location!="Unset"){
            userLocation=location;
        }
    }
    public void setTimeAt(String time){
        if(time!=null && time!="Unset"){
            insertedAt=time;
        }
    }
}
