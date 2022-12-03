package edu.northeastern.numad22fa_team12.outfitTodayModel;

import java.io.Serializable;

public class Outfit implements Serializable {
    private int categoryId;
    private String url;
    private String itemId;
    private String userId;
    private int seasonId;
    private int occasionId;

    public Outfit(int categoryId, String url, String itemId, String userId, int seasonId, int occasionId) {
        this.categoryId = categoryId;
        this.url = url;
        this.itemId = itemId;
        this.userId = userId;
        this.seasonId = seasonId;
        this.occasionId = occasionId;
    }


    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public int getOccasionId() {
        return occasionId;
    }

    public void setOccasionId(int occasionId) {
        this.occasionId = occasionId;
    }
}
