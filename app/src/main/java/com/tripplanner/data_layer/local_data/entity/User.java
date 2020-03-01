package com.tripplanner.data_layer.local_data.entity;



import java.util.ArrayList;
import java.util.List;

public class User {

   private String id;
   private String name;
   private String profileUrl;
   private List<Trip> trips;


    public User(String id, String name, String profileUrl) {
        this.id = id;
        this.name = name;
        this.profileUrl = profileUrl;
       trips=new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", profileUrl='" + profileUrl + '\'' +
                ", trips=" + trips +
                '}';
    }
}
