package edu.northeastern.numad22fa_team12.outfitTodayModel;

public class Friend {
    private String name;
    private String realTimeChat;

    public Friend() {
        this.name = "";
        this.realTimeChat = "";
    }

    public Friend(String name, String realTimeChat) {
        this.name = name;
        this.realTimeChat = realTimeChat;
    }

    public String getName() {
        return this.name;
    }

    public String getBeFriendTime() {
        return this.realTimeChat;
    }
}
