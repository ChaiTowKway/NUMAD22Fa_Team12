package edu.northeastern.numad22fa_team12.outfitTodayModel;

public class Item {
    private int season;
    private int occasion;
    private int category;
    private String imageUrl;

    public Item() {
        this.season = 0;
        this.occasion = 0;
        this.category = 0;
        this.imageUrl = "";
    }

    public Item(int season, int occasion, int category, String imageUrl) {
        this.season = season;
        this.occasion = occasion;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public int getSeason() {
        return this.season;
    }

    public int getOccasion() {
        return this.occasion;
    }

    public int getCategory() {
        return this.category;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public void setOccasion(int occasion) {
        this.occasion = occasion;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
