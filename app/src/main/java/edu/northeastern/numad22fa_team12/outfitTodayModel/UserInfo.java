package edu.northeastern.numad22fa_team12.outfitTodayModel;

public class UserInfo {
    private String userName;
    private String email;
    private String UID;
    private String contactNumber;
    private String location;
    private int numOfCheckInDates;
    private String userProfileImageUrl;

    public UserInfo() {
        this.userName = "";
        this.email = "";
        this.UID = "";
        this.contactNumber = "xxx-xxx-xxx";
        this.location = "";
        this.numOfCheckInDates = 0;
        this.userProfileImageUrl = "";
    }

    public UserInfo(String userName, String email, String UID) {
        this.userName = userName;
        this.email= email;
        this.UID = UID;
        this.contactNumber = "xxx-xxx-xxx";
        this.location = "";
        this.numOfCheckInDates = 0;
        this.userProfileImageUrl = "";
    }

    public UserInfo(String userName, String email, String UID, String contactNumber) {
        this.userName = userName;
        this.email= email;
        this.UID = UID;
        this.contactNumber = contactNumber;
        this.location = "";
        this.numOfCheckInDates = 0;
        this.userProfileImageUrl = "";
    }

    public UserInfo(String userName, String email, String UID, String contactNumber, String location) {
        this.userName = userName;
        this.email= email;
        this.UID = UID;
        this.contactNumber = contactNumber;
        this.location = location;
        this.numOfCheckInDates = 0;
        this.userProfileImageUrl = "";
    }


    public UserInfo(String userName, String email, String UID, String contactNumber,
                    String location, String userProfileImageUrl) {
        this.userName = userName;
        this.email= email;
        this.UID = UID;
        this.contactNumber = contactNumber;
        this.location = location;
        this.numOfCheckInDates = 0;
        this.userProfileImageUrl = userProfileImageUrl;
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

    public int getNumOfCheckInDates() {
        return this.numOfCheckInDates;
    }

    public String getUserProfileImageUrl() {
        return this.userProfileImageUrl;
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

    public void setNumOfCheckInDates(int numOfCheckInDates) {
        this.numOfCheckInDates = numOfCheckInDates;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }
}
