package edu.northeastern.numad22fa_team12.outfitTodayModel;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Outfit implements Serializable , Parcelable {
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


    protected Outfit(Parcel in) {
        categoryId = in.readInt();
        url = in.readString();
        itemId = in.readString();
        userId = in.readString();
        seasonId = in.readInt();
        occasionId = in.readInt();
    }

    public static final Creator<Outfit> CREATOR = new Creator<Outfit>() {
        @Override
        public Outfit createFromParcel(Parcel in) {
            return new Outfit(in);
        }

        @Override
        public Outfit[] newArray(int size) {
            return new Outfit[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(categoryId);
        parcel.writeString(url);
        parcel.writeString(itemId);
        parcel.writeString(userId);
        parcel.writeInt(seasonId);
        parcel.writeInt(occasionId);
    }
}
