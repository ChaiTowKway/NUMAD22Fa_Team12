package edu.northeastern.numad22fa_team12.model;

public class User {

    private String userName;
    private String userEmail;
    private int numberOfStickersSent;
    private int numberOfStickersReceived;

    public User(String userEmail, String userName) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.numberOfStickersSent = 0;
        this.numberOfStickersReceived = 0;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getNumberOfStickersSent() {
        return numberOfStickersSent;
    }

    public int getNumberOfStickersReceived() {
        return numberOfStickersReceived;
    }
}
