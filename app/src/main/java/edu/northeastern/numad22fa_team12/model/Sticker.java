package edu.northeastern.numad22fa_team12.model;

public class Sticker {

    private int stickerID;
    private String sentByUser;
    private String receivedByUser;
    private String dateAndTime;

    public Sticker() {
        this.stickerID = 0;
        this.sentByUser = "";
        this.receivedByUser = "";
        this.dateAndTime = "";

    }

    public Sticker(int stickerID, String sentByUser, String receivedByUser, String dateAndTime) {
        this.stickerID = stickerID;
        this.sentByUser = sentByUser;
        this.receivedByUser = receivedByUser;
        this.dateAndTime = dateAndTime;
    }

    public int getStickerID() {
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
