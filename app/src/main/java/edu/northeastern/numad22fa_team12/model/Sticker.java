package edu.northeastern.numad22fa_team12.model;

public class Sticker {

    private String stickerID;
    private String totalUse;


    public Sticker(String stickerID, String totalUse) {
        this.stickerID = stickerID;
        this.totalUse = totalUse;
    }

    public String getStickerID() {
        return stickerID;
    }

    public String getTotalUse() {
        return totalUse;
    }
}
