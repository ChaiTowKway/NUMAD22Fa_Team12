package edu.northeastern.numad22fa_team12.outfitTodayModel;

public class Item {
    private String season;
    private String occasion;
    private String category;
    private String imageUrl;
    private boolean openForSwap;

    public Item() {
        this.season = "winter";
        this.occasion = "work";
        this.category = "tops";
        this.imageUrl = "url";
        this.openForSwap = false;
    }

    public Item(String season, String occasion, String category, String imageUrl, boolean openForSwap) {
        this.season = season;
        this.occasion = occasion;
        this.category = category;
        this.imageUrl = imageUrl;
        this.openForSwap = openForSwap;
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

    public boolean getOpenForSway() {
        return this.openForSwap;
    }

}
