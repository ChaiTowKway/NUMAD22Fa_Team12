package edu.northeastern.numad22fa_team12.outfitTodayModel;

public class Friend {
    private String name;
    private String beFriendTime;

    public Friend() {
        this.name = "";
        this.beFriendTime = "";
    }

    public Friend(String name, String beFriendTime) {
        this.name = name;
        this.beFriendTime = beFriendTime;
    }

    public String getName() {
        return this.name;
    }

    public String getBeFriendTime() {
        return this.beFriendTime;
    }
}
