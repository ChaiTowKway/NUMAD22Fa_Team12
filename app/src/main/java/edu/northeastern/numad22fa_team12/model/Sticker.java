package edu.northeastern.numad22fa_team12.model;

public class Sticker {

    private String stickerID;
    private String sentByUser;
    private String receivedByUser;
    private String dateAndTime;

    public Sticker(String stickerID, String sentByUser, String receivedByUser, String dateAndTime) {
        this.stickerID = stickerID;
        this.sentByUser = sentByUser;
        this.receivedByUser = receivedByUser;
        this.dateAndTime = dateAndTime;
    }

    public String getStickerID() {
        return this.stickerID;
    }

    public String getSentByUser() {
        return this.sentByUser;
    }

    public String getReceivedByUser() {
        return this.receivedByUser;
    }

    public String getDateAndTime() {
        return this.dateAndTime;
    }

}
