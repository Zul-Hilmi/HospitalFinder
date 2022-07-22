package com.example.hospitalfinder;

public class User {

    String userName;
    String userLocation;
    String userCoordinate;
    InsertedAt insertedAt;
    String userAgent;

    public User(){}

    public User(String userName) {
        this.userName = userName;
        this.userLocation = "Unset";
        this.userCoordinate = "Unset";
        this.insertedAt= new InsertedAt();
        this.insertedAt.setDate("Unset");
        this.insertedAt.setTime("Unset");
        this.userAgent="Unset";
    }

    public String getUserName() {return userName;}
    public String getUserLocation(){return userLocation;}
    public String getUserCoordinate(){return userCoordinate;}
    public InsertedAt getInsertedAt(){return insertedAt;}
    public String getUserAgent(){return userAgent;}


    public void setUserName(String userName) {
        if(userName!=null && userName!="Unset"){
            this.userName=userName;
        }
    }

    public void setUserLocation(String location){
        if(location!=null && location!="Unset"){
            userLocation=location;
        }
    }

    public void setUserCoordinate(String userCoordinate) {
        if(userCoordinate!=null && userCoordinate!="Unset"){
            this.userCoordinate=userCoordinate;
        }
    }

    public void setInsertedAt(String date, String time){
        if(time!=null && time!="Unset"){
            insertedAt.setTime(time);
        }
        if(date!=null && date!="Unset"){
            insertedAt.setDate(date);
        }
    }

    public void setUserAgent(String userAgent) {
        if(userAgent!=null && userAgent!="Unset"){
            this.userAgent=userAgent;
        }
    }
}
