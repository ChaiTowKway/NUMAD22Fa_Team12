package edu.northeastern.numad22fa_team12.outfitTodayModel;

import java.util.HashMap;

public class OutfitTodayUser {

    private UserInfo userInfo;
    private OccasionsList occasionsList;
    private CategoryList categoryList;
    private HashMap<String, Item> wardrobe;
    private String wardrobeViewBy;

    public OutfitTodayUser() {
        this.userInfo = new UserInfo();
        this.occasionsList = new OccasionsList();
        this.categoryList = new CategoryList();
        this.wardrobe = new HashMap<>();
        this.wardrobe.put("url", new Item());
        this.wardrobeViewBy = "";
    }

    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    public OccasionsList getOccasionsList() {
        return occasionsList;
    }

    public CategoryList getCategoryList() {
        return this.categoryList;
    }

    public HashMap<String, Item> getWardrobe() {
        return this.wardrobe;
    }

    public String getWardrobeViewBy() {
        return this.wardrobeViewBy;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public void setOccasionsList(OccasionsList occasionsList) {
        this.occasionsList = occasionsList;
    }

    public void setCategoryList(CategoryList categoryList) {
        this.categoryList = categoryList;
    }

    public void setWardrobe(HashMap<String, Item> wardrobe) {
        this.wardrobe = wardrobe;
    }

    public void setWardrobeViewBy(String wardrobeViewBy) {
        this.wardrobeViewBy = wardrobeViewBy;
    }
}
