package edu.northeastern.numad22fa_team12.outfitTodayModel;

public class UserInfo {
    private String userName;
    private String email;
    private String UID;
    private String contactNumber;
    private String location;

    public UserInfo() {
        this.userName = "Default";
        this.email = "Default@gmail.com";
        this.UID = "";
        this.contactNumber = "1-xxx-xxx-xxx";
        this.location = "";
    }

    public UserInfo(String userName, String email, String UID, String contactNumber, String location) {
        this.userName = userName;
        this.email= email;
        this.UID = UID;
        this.contactNumber = contactNumber;
        this.location = location;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUID() {
        return this.UID;
    }

    public String getContactNumber() {
        return this.contactNumber;
    }

    public String getLocation() {
        return this.location;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
