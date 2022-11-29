package edu.northeastern.numad22fa_team12.outfitTodayModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OutfitTodayUser {

    private UserInfo userInfo;
    private OccasionsList occasionsList;
    private CategoryList categoryList;
    private HashMap<String, Item> wardrobe;
//    private HashMap<String, Friend> friends;

    public OutfitTodayUser() {
        this.userInfo = new UserInfo();
        this.occasionsList = new OccasionsList();
        this.categoryList = new CategoryList();
        this.wardrobe = new HashMap<>();
//        this.friends = new HashMap<>();
        this.wardrobe.put("url", new Item());
//        this.friends.put("User2", new Friend());
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

//    public HashMap<String, Friend> getFriends() {
//        return this.friends;
//    }

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

}
