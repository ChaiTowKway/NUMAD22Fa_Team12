package edu.northeastern.numad22fa_team12.outfitTodayModel;

public class Item {
    private String season;
    private String occasion;
    private String category;
    private String imageUrl;

    public Item() {
        this.season = "winter";
        this.occasion = "work";
        this.category = "tops";
        this.imageUrl = "url";
    }

    public Item(String season, String occasion, String category, String imageUrl) {
        this.season = season;
        this.occasion = occasion;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public String getSeason() {
        return this.season;
    }

    public String getOccasion() {
        return this.occasion;
    }

    public String getCategory() {
        return this.category;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

}
