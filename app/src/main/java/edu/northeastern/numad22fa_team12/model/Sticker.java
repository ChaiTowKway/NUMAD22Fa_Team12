package edu.northeastern.numad22fa_team12.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    @NonNull
    @Override
    public String toString() {
        return "id: " + stickerID + " time: " + dateAndTime;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Sticker)) return false;
        Sticker other = (Sticker) obj;
        return other.getStickerID() == this.getStickerID()
                && other.getSentByUser() == this.getSentByUser()
                && other.getReceivedByUser() == this.getReceivedByUser()
                && other.getDateAndTime() == this.getDateAndTime();
    }
}
