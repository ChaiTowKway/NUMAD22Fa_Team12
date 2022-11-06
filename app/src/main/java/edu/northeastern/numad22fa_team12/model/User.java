package edu.northeastern.numad22fa_team12.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String userName;
    private String userEmail;
//    private String userRegistrationToken;
    private List<Sticker> sentStickerList;
    private int numberOfStickersSent;
    private List<Sticker> receivedStickerList;
    private int numberOfStickersReceived;

    public User(String userEmail, String userName) {
        this.userEmail = userEmail;
        this.userName = userName;
//        this.userRegistrationToken = userRegistrationToken;
        this.sentStickerList = new ArrayList<>();
        this.numberOfStickersSent = 0;
        this.receivedStickerList = new ArrayList<>();
        this.numberOfStickersReceived = 0;
    }

    public String getUserName() {
        return this.userName;
    }

//    public String getUserRegistrationToken() {
//        return this.userRegistrationToken;
//    }

    public List<Sticker> getSentStickerList() {
        return new ArrayList<>(this.sentStickerList);
    }

    public List<Sticker> getReceivedStickerList() {
        return new ArrayList<>(this.receivedStickerList);
    }

    public int getNumberOfStickersSent() {
        return this.sentStickerList.size();
    }

    public int getNumberOfStickersReceived() {
        return this.receivedStickerList.size();
    }

    public void setSentStickerList(Sticker stickerSent) {
        this.sentStickerList.add(stickerSent);
    }

    public void setReceivedStickerList(Sticker stickerReceived) {
        this.receivedStickerList.add(stickerReceived);
    }

    public String getUserEmail() {
        return userEmail;
    }
}
