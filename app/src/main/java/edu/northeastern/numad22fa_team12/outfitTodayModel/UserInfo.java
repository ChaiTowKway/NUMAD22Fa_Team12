package edu.northeastern.numad22fa_team12.outfitTodayModel;

import java.util.HashMap;

public class UserInfo {
    private String userName;
    private String email;
    private String contactNumber;
    private HashMap<String, Double> location;


    public UserInfo() {
        this.userName = "";
        this.email = "";
        this.contactNumber = "";
        this.location = new HashMap<>();
        this.location.put("latitude", 0.0);
        this.location.put("longitude", 0.0);
    }

    public String getUserName() {
        return this.userName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getContactNumber() {
        return this.contactNumber;
    }

    public HashMap<String, Double> getLocation() {
        return this.location;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setLocation(HashMap<String, Double> location) {
        this.location = location;
    }

}
